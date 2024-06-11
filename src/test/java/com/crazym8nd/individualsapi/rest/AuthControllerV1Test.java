package com.crazym8nd.individualsapi.rest;

import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.util.AuthUtils;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.yaml")
class AuthControllerV1Test {

    @Container
    KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4")
            .withEnv("DB_VENDOR", "h2")
            .withEnv("DB_URL", "jdbc:h2:mem:testdb")
            .withEnv("DB_USER", "sa")
            .withEnv("DB_PASSWORD", "")
            .withRealmImportFile("realm-export-test.json");

    @Bean
    @Primary
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakContainer.getAuthServerUrl())
                .realm("appauth")
                .clientId(KeycloakContainer.ADMIN_CLI_CLIENT)
                .username(keycloakContainer.getAdminUsername())
                .password(keycloakContainer.getAdminPassword())
                .build();
    }


    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Load context")
    void loadContext() {


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