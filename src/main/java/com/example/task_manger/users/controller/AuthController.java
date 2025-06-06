package com.example.task_manger.users.controller;

import com.example.task_manger.users.request.LoginRequest;
import com.example.task_manger.users.request.RefreshTokenRequest;
import com.example.task_manger.users.request.RegisterRequest;
import com.example.task_manger.users.service.AuthService;
import com.example.task_manger.util.exceptions.APIException;
import com.example.task_manger.util.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "User Register",
            description = "Register user by first name, last name, username, email and password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"username\": \"abdallah\", \"firstName\": \"AbdAllah\", \"lastName\": \"Alharisi\", \"email\": \"abd@gmail.com\", \"password\": \"Abd@1234\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registration successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterRequest.class),
                                    examples = @ExampleObject("""
                                            {
                                                  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5VVluazZqdXBUUV9HaWtGTFB3OTJtUTBFTzJROFN5RS02X2RnM3VvV2tBIn0.eyJleHAiOjE3NDcwNDU0MjQsImlhdCI6MTc0NzA0NTEyNCwianRpIjoib25ydHJvOmU5NDUwNzE0LWE5MGQtNDM5Mi05NGU0LWNlY2M5YmEzYjhlOCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvVGVzdE1hbmdlclVzZXJzIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjY4NTUzYzI2LWUyYWMtNGY1Yi1iZWQ2LTVhYWYxZThjYjI1ZiIsInR5cCI6IkJlYXJlciIsImF6cCI6InRhc2stbWFuZ2VyLWFwaSIsInNpZCI6IjMwNzM1MmNkLWExMjAtNDI4Mi05NjNmLWExODQzMzg5OWFiNyIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy10ZXN0bWFuZ2VydXNlcnMiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfSwidGFzay1tYW5nZXItYXBpIjp7InJvbGVzIjpbImNsaWVudF91c2VyIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6Im1lZG8gYWxhYmQiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhYmRhbGxhaDIiLCJnaXZlbl9uYW1lIjoibWVkbyIsImZhbWlseV9uYW1lIjoiYWxhYmQiLCJlbWFpbCI6ImFiZGFsbGFoMkBnbWFpbC5jb20ifQ.YMhAKuKVvTA0Hw4jKyCIzdKO1Bwn-F6c9GmBaMLE43Wv6zaws22MK0Jki0csd982OjuWs3CWDLFxL-FpMxeBmraV26HzRMZtqFi0UC3cZZ0BHiMQymZpYean74NSH1llUnU2LSlaC0vWbNqVZeEhbzI69OjdlYePQpsmxn867HGlHH0dn9qoLrUQoQ5bp_wzdXNYEMUblP6y3NwPp2mbnXWKnLSH8_lxHyyZPoz1SiWbRYf6zkZ2iyBK0T8Rk9-ax_XX7gy4KMm5e0tr-z2ha86FMchEf59DwAbkINt97R9L1DQZFNh0g_53rYmqUgNOQ7-EjuGgs5D98hOpVFPdpQ",
                                                  "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MmIxNzk4Zi02OTQ5LTRkNTctODliNS1mMzk1MWFkZDY5MzQifQ.eyJleHAiOjE3NDcwNDY5MjQsImlhdCI6MTc0NzA0NTEyNCwianRpIjoiYTIwNzgxZGUtMTA2Yi00NDllLWFhODUtYjBlZjBmNTk4YmM1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9UZXN0TWFuZ2VyVXNlcnMiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvcmVhbG1zL1Rlc3RNYW5nZXJVc2VycyIsInN1YiI6IjY4NTUzYzI2LWUyYWMtNGY1Yi1iZWQ2LTVhYWYxZThjYjI1ZiIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJ0YXNrLW1hbmdlci1hcGkiLCJzaWQiOiIzMDczNTJjZC1hMTIwLTQyODItOTYzZi1hMTg0MzM4OTlhYjciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIHdlYi1vcmlnaW5zIGFjciByb2xlcyBzZXJ2aWNlX2FjY291bnQgYmFzaWMgZW1haWwifQ.WmOKcuBBrV6A-kYlsyTeYVaNIMYqwDvpXsHq20GXYr8epHG31ysXdlAgwuMUppbpIjwwHafWFxr_05lscXhkDg",
                                                  "firstName": "AbdAllah",
                                                  "lastName": "Alharisi",
                                                  "id": 324asd-12scaw-asdac1-csasda,
                                                  "email": "abd@gmail.com",
                                                  "username": "Abd@1234"
                                              }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIException.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Email already exists",
                          "status": "CONFLICT",
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<MyAPIResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        boolean success = authService.registerUser(request);
        return success ? ResponseEntity.ok(new MyAPIResponse<>(true,201,"User registered successfully"))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MyAPIResponse<>(false,400,"Registration failed"));
    }

    @Operation(
            summary = "User Login",
            description = "Login user by first username and password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"username\": \"abdallah\", \"password\": \"Abd@1234\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterRequest.class),
                                    examples = @ExampleObject("""
                                            {
                                                  "firstName": "AbdAllah",
                                                  "lastName": "Alharisi",
                                                  "id": "68553c26-e2ac-4f5b-bed6-5aaf1e8cb25f",
                                                  "expires_in": 300,
                                                  "email": "abd@gmail.com",
                                                  "username": "abdallah2",
                                                  "password": "Abd@1234",
                                                  "refresh_token": "eyJhbGciOiJbpIjwwHafWFxr....."
                                                  "access_token": "eyJhbGciOiJSUzI1NiIsIOpVF.....",
                                              }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid Email or password",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIException.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Invalid Email or password",
                          "error": "UNAUTHORIZED",
                          "status": 401,
                          "timestamp": "2025-05-12T15:35:36.3562089",
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    @Operation(
            summary = "Delete User",
            description = "Delete user by jwt token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterRequest.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "User deleted successfully",
                          "status": 200,
                        }
                        """)
                            ))}

    )
    @DeleteMapping("/delete")
    public ResponseEntity<MyAPIResponse<?>> deleteUser(@AuthenticationPrincipal Jwt jwt) {
            return authService.deleteUser(jwt);
    }

    @Operation(
            summary = "Logout User",
            description = "Logout user by jwt token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged out successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterRequest.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "User logged out successfully",
                          "status": 200,
                        }
                        """)
                            ))}
    )
    @PostMapping("/logout")
    public ResponseEntity<MyAPIResponse<String>> logout(@AuthenticationPrincipal Jwt jwt) {
        boolean success = authService.logoutUser(jwt);
        return success ?ResponseEntity.ok(new MyAPIResponse<>(true,200,"User logged out successfully."))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MyAPIResponse<>(false,400,"Logout faild"));
    }

    @Operation(
            summary = "Refresh Access Token",
            description = "Refresh access token by refresh token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "refreshToken": "eyJhbGciOiJSUzI1NiIsIOpVF....."
                                    }
                        """)
                    )
                            )
    )
    @PostMapping("/refresh")
    public ResponseEntity<MyAPIResponse<String>> refreshAccessToken(@RequestBody RefreshTokenRequest refreshToken) {
        return authService.refreshToken(refreshToken);
    }


}
