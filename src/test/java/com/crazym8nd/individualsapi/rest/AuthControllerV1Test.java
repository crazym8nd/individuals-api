package com.crazym8nd.individualsapi.rest;

import com.crazym8nd.individualsapi.config.KeycloakTestContainers;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.util.AuthUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.yaml")
class AuthControllerV1Test extends KeycloakTestContainers {

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