package com.example.task_manger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Keycloak properties it to get any confidential variable from keycloak
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class KeycloakProperties {

    private Resourceserver resourceserver = new Resourceserver();
    private Client client = new Client();

    public Resourceserver getResourceserver() {
        return resourceserver;
    }

    public void setResourceserver(Resourceserver resourceserver) {
        this.resourceserver = resourceserver;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class Client{
        private Registration registration = new Registration();
        private Provider provider = new Provider();

        public Registration getRegistration() {
            return registration;
        }

        public void setRegistration(Registration registration) {
            this.registration = registration;
        }

        public Provider getProvider() {
            return provider;
        }

        public void setProvider(Provider provider) {
            this.provider = provider;
        }

        public static class Registration {
            private Keycloak keycloak = new Keycloak();

            public Keycloak getKeycloak() {
                return keycloak;
            }

            public void setKeycloak(Keycloak keycloak) {
                this.keycloak = keycloak;
            }

            public static class Keycloak {
                private String clientId;
                private String clientSecret;
                private String authorizationGrantType;
                private String scope;
                private String realm;

                public String getClientId() {
                    return clientId;
                }

                public void setClientId(String clientId) {
                    this.clientId = clientId;
                }

                public String getClientSecret() {
                    return clientSecret;
                }

                public void setClientSecret(String clientSecret) {
                    this.clientSecret = clientSecret;
                }

                public String getAuthorizationGrantType() {
                    return authorizationGrantType;
                }

                public void setAuthorizationGrantType(String authorizationGrantType) {
                    this.authorizationGrantType = authorizationGrantType;
                }

                public String getScope() {
                    return scope;
                }

                public void setScope(String scope) {
                    this.scope = scope;
                }

                public String getRealm() {
                    return realm;
                }

                public void setRealm(String realm) {
                    this.realm = realm;
                }
            }
        }

        public static class Provider {
            private Keycloak keycloak = new Keycloak();

            public Keycloak getKeycloak() {
                return keycloak;
            }

            public void setKeycloak(Keycloak keycloak) {
                this.keycloak = keycloak;
            }

            public static class Keycloak {
                private String issuerUri;
                private String userUri;
                private String userInfoUri;
                private String authorizationUri;
                private String adminTokenUri;
                private String tokenUri;
                private String jwkSetUri;
                private String keycloakUri;
                private String userNameAttribute;

                public String getIssuerUri() {
                    return issuerUri;
                }

                public void setIssuerUri(String issuerUri) {
                    this.issuerUri = issuerUri;
                }

                public String getUserUri() {
                    return userUri;
                }

                public void setUserUri(String userUri) {
                    this.userUri = userUri;
                }

                public String getUserInfoUri() {
                    return userInfoUri;
                }

                public void setUserInfoUri(String userInfoUri) {
                    this.userInfoUri = userInfoUri;
                }

                public String getAuthorizationUri() {
                    return authorizationUri;
                }

                public void setAuthorizationUri(String authorizationUri) {
                    this.authorizationUri = authorizationUri;
                }

                public String getAdminTokenUri() {
                    return adminTokenUri;
                }

                public void setAdminTokenUri(String adminTokenUri) {
                    this.adminTokenUri = adminTokenUri;
                }

                public String getTokenUri() {
                    return tokenUri;
                }

                public void setTokenUri(String tokenUri) {
                    this.tokenUri = tokenUri;
                }

                public String getJwkSetUri() {
                    return jwkSetUri;
                }

                public void setJwkSetUri(String jwkSetUri) {
                    this.jwkSetUri = jwkSetUri;
                }

                public String getUserNameAttribute() {
                    return userNameAttribute;
                }

                public void setUserNameAttribute(String userNameAttribute) {
                    this.userNameAttribute = userNameAttribute;
                }

                public String getKeycloakUri() {
                    return keycloakUri;
                }

                public void setKeycloakUri(String keycloakUri) {
                    this.keycloakUri = keycloakUri;
                }
            }
        }
    }

    public static class Resourceserver {
        private Resource resource = new Resource();

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public static class Resource {
            private String username;
            private String password;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }
    }

}
