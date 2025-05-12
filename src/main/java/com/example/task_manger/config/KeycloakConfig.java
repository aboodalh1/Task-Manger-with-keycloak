package com.example.task_manger.config;

import org.keycloak.OAuth2Constants;

import org.keycloak.admin.client.Keycloak;

import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    private final KeycloakProperties keycloakProperties;

    public KeycloakConfig(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder().password(keycloakProperties.getResourceserver().getResource().getPassword())
                .username(keycloakProperties.getResourceserver().getResource().getUsername())
                .serverUrl(keycloakProperties.getClient().getProvider().getKeycloak().getKeycloakUri())
                .realm(keycloakProperties.getClient().getRegistration().getKeycloak().getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakProperties.getClient().getRegistration().getKeycloak().getClientId())
                .clientSecret(keycloakProperties.getClient().getRegistration().getKeycloak().getClientSecret())
                .build();
    }
}