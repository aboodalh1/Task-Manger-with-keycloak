package com.example.task_manger.users.service;

import com.example.task_manger.config.KeycloakProperties;
import com.example.task_manger.config.UserContext;
import com.example.task_manger.users.request.LoginRequest;
import com.example.task_manger.users.request.RefreshTokenRequest;
import com.example.task_manger.users.request.RegisterRequest;
import com.example.task_manger.util.exceptions.ConflictException;
import com.example.task_manger.util.exceptions.UnAuthorizedException;
import com.example.task_manger.util.response.MyAPIResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String scope = "openid profile email";

    @Autowired
    private Keycloak keycloak;

    private final KeycloakProperties keycloakProperties;

    public AuthService(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }


    public boolean registerUser(RegisterRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAdminToken());

        Map<String, Object> body = new HashMap<>();
        body.put("username", request.getUsername());
        body.put("email", request.getEmail());
        body.put("firstName", request.getFirstName());
        body.put("lastName", request.getLastName());
        body.put("enabled", true);

        List<Map<String, Object>> credentials = List.of(Map.of(
                "type", "password",
                "value", request.getPassword(),
                "temporary", false
        ));
        body.put("credentials", credentials);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    keycloakProperties.getClient().getProvider().getKeycloak().getUserUri(),
                    entity,
                    String.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                URI location = response.getHeaders().getLocation();
                String path = location.getPath();
                String userId = path.substring(path.lastIndexOf("/") + 1);
                assignUserRole(userId);
                return true;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                // user already exists
                throw new ConflictException("Username of email exist");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
            }
        }

        return false;
    }

    public ResponseEntity<?> loginUser(LoginRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", keycloakProperties.getClient().getRegistration().getKeycloak().getClientId());
        form.add("client_secret", keycloakProperties.getClient().getRegistration().getKeycloak().getClientSecret());
        form.add("username", request.getUsername());
        form.add("password", request.getPassword());
        form.add("scope", scope);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        try {


            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                    keycloakProperties.getClient().getProvider().getKeycloak().getTokenUri(),
                    entity,
                    Map.class
            );


            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login");
            }

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            // Step 2: Use token to get user info
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> userInfoEntity = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    keycloakProperties.getClient().getProvider().getKeycloak().getUserInfoUri(),
                    HttpMethod.GET,
                    userInfoEntity,
                    Map.class
            );

            Map<String, Object> result = new HashMap<>();
            if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                result.put("id", userInfo.get("sub"));
                result.put("firstName", userInfo.get("given_name"));
                result.put("lastName", userInfo.get("family_name"));
                result.put("username", userInfo.get("preferred_username"));
                result.put("email", userInfo.get("email"));
            result.put("access_token", accessToken);
            result.put("refresh_token", tokenResponse.getBody().get("refresh_token"));
            result.put("expires_in", tokenResponse.getBody().get("expires_in"));

            }

            return ResponseEntity.ok(result);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {

                throw new UnAuthorizedException("Invalid Email or password");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
            }
        }

    }

    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "admin-cli");
        form.add("username", keycloakProperties.getResourceserver().getResource().getUsername());
        form.add("password", keycloakProperties.getResourceserver().getResource().getPassword());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                keycloakProperties.getClient().getProvider().getKeycloak().getAdminTokenUri(),
                entity,
                Map.class
        );
        return (String) response.getBody().get("access_token");
    }


    public ResponseEntity<String> deleteUser(Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            System.out.println(userId);
            keycloak.realm(keycloakProperties.getClient().getRegistration().getKeycloak().getRealm())
                    .users()
                    .get(userId)
                    .logout();  // ends all sessions
            UsersResource userResource = keycloak.realm(keycloakProperties.getClient().getRegistration().getKeycloak().getRealm()).users();
            userResource.delete(userId);
            UserContext.clear();
            return ResponseEntity.ok("User deleted successfully.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

    public void assignUserRole(String userId) {

        try {
            System.out.println(keycloak.serverInfo().getInfo());
        } catch (Exception e) {
            return;
        }

        try {
            RoleRepresentation userRole = keycloak.realm(keycloakProperties.getClient().getRegistration().getKeycloak().getRealm())
                    .roles()
                    .get("user")
                    .toRepresentation();
            keycloak.realm(keycloakProperties.getClient().getRegistration().getKeycloak().getRealm())
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(List.of(userRole));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean logoutUser(Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            keycloak.realm(keycloakProperties.getClient().getRegistration().getKeycloak().getRealm())
                    .users()
                    .get(userId)
                    .logout();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity<MyAPIResponse<String>> refreshToken(RefreshTokenRequest refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken.getRefreshToken());
        body.add("client_id", keycloakProperties.getClient().getRegistration().getKeycloak().getClientId());
        body.add("client_secret", keycloakProperties.getClient().getRegistration().getKeycloak().getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        try{

        ResponseEntity<Map> response = restTemplate.postForEntity(
                keycloakProperties.getClient().getProvider().getKeycloak().getTokenUri(),
                requestEntity,
                Map.class
        );
        return ResponseEntity.ok(new MyAPIResponse<>(true, 200, response.getBody().get("access_token").toString()));
        }catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadRequestException("Bad");
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UnAuthorizedException("Invalid Refresh token");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
            }
        }

    }


}
