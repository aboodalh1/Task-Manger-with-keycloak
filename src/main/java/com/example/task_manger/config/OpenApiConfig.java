package com.example.task_manger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

// Swagger configuration

@OpenAPIDefinition(
        info =@Info(
                contact = @Contact(
                        name = "AbdAllah Alharisi" ,
                        email = "AbdAllahAlharisy@gmail.com"
                ),
                description = "Documentation for Task Manger System",
                title = "Task-Manger-System",
                version = "1.0"
        ),
        servers =
                {
                        @Server(
                                description = "Local ENV",
                                url = "http://localhost:8081"
                        )
                },
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT & OAtuh 2.0 auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
