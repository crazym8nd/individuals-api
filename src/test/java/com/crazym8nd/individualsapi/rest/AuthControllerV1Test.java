package com.crazym8nd.individualsapi.rest;

import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.service.KeycloakService;
import com.crazym8nd.individualsapi.util.AuthUtils;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class AuthControllerV1Test {


    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private WebTestClient webTestClient;

    static KeycloakContainer keycloak;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeEach
    void setUp() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
        if (keycloak == null) {
            keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4")
                    .withEnv("DB_VENDOR", "POSTGRES")
                    .withEnv("DB_ADDR", postgres.getHost())
                    .withEnv("DB_PORT", String.valueOf(postgres.getMappedPort(5432)))
                    .withEnv("DB_DATABASE", postgres.getDatabaseName())
                    .withEnv("DB_USER", postgres.getUsername())
                    .withEnv("DB_PASSWORD", postgres.getPassword())
                    .withRealmImportFile("realm-export-test.json");
            keycloak.start();
        }
        System.setProperty("keycloak.clientId", "app-auth-client-id");
        System.setProperty("keycloak.clientSecret", "FuXDordwvRlgaCasQ1xsqi6s9pkAuQPI");
        System.setProperty("keycloak.urls.auth", keycloak.getAuthServerUrl());

    }

    @Test
    @DisplayName("Should return 201 response")
    public void givenValidUserRegistration_whenRegisterMerchant_thenOKResponse() {

        // Given
        UserRegistration validUserRegistration = AuthUtils.jsonForCreatingMerchantTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register/merchants")
               .body(Mono.just(validUserRegistration), UserRegistration.class)
               .exchange();

        // Then
        result.expectStatus().isCreated();
    }
}