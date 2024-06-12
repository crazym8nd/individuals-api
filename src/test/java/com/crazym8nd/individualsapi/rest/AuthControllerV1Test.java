package com.crazym8nd.individualsapi.rest;

import com.crazym8nd.individualsapi.config.KeycloakTestContainers;
import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
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


    //happy path
    @Test
    @DisplayName("Should return 201 response fore creation of merchant")
    public void givenValidMerchantRegistration_whenRegisterMerchant_then201Response() {

        // Given
        UserRegistration validUserRegistration = AuthUtils.jsonForCreatingMerchantTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register/merchants")
               .body(Mono.just(validUserRegistration), UserRegistration.class)
               .exchange();

        // Then
        result.expectStatus().isCreated();
    }
    @Test
    @DisplayName("Should return 201 response fore creation of individual")
    public void givenValidIndividualRegistration_whenRegisterIndividual_then201Response() {

        // Given
        UserRegistration validUserRegistration = AuthUtils.jsonForCreatingIndividualTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register/individuals")
                .body(Mono.just(validUserRegistration), UserRegistration.class)
                .exchange();

        // Then
        result.expectStatus().isCreated();
    }

    @Test
    @DisplayName("Should return valid access token")
    public void givenValidLoginRequest_whenLogin_thenGetAccessToken() {
        // Given
        WebTestClient.ResponseSpec register = webTestClient.post().uri("/api/v1/auth/register/merchants")
                .body(Mono.just(AuthUtils.registrationForLoginTest()), UserRegistration.class)
                .exchange();
        LoginRequest validLoginRequest = AuthUtils.jsonForLoginTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/login")
                .body(Mono.just(validLoginRequest), LoginRequest.class)
                .exchange();

        // Then
        result.expectStatus().isOk();
    }



    @Test
    @DisplayName("Should return information about user")
    public void givenValidToken_whenGettingUserInfo_thenReturnInfo() {
        // Given
        WebTestClient.ResponseSpec register = webTestClient.post().uri("/api/v1/auth/register/merchants")
                .body(Mono.just(AuthUtils.registrationForLoginTest()), UserRegistration.class)
                .exchange();
        LoginRequest validLoginRequest = AuthUtils.jsonForLoginTest();
        WebTestClient. ResponseSpec response = webTestClient.post().uri("/api/v1/auth/login")
                .body(Mono.just(validLoginRequest), LoginRequest.class)
                .exchange();

        ResponseTokenLogin token = response
                .expectStatus().isOk()
                .expectBody(ResponseTokenLogin.class)
                .returnResult().getResponseBody();
        String tokenValue = token.getAccessToken();

        // When
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/auth/info/me")
                .headers(headers -> headers.setBearerAuth(tokenValue))
                .exchange();

        // Then
        result.expectStatus().isOk()
                .expectBody(ResponseInfo.class);
    }

    // unhappy path
    @Test
    @DisplayName("Should return not authorized if token is not valid")
    public void givenInvalidToken_whenGettingUserInfo_thenReturnNotAuthorized() {
        // Given
        String tokenValue = "wrongtokenXyz";

        // When
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/auth/info/me")
                .headers(headers -> headers.setBearerAuth(tokenValue))
                .exchange();

        // Then
        result.expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Should return 400 response for error creation of individual")
    public void givenInvalidIndividualRegistration_whenRegisterIndividual_thenBadRequestResponse() {

        // Given
        UserRegistration validUserRegistration = AuthUtils.invalidJsonForCreatingTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register/individuals")
                .body(Mono.just(validUserRegistration), UserRegistration.class)
                .exchange();

        // Then
        result.expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should return 400 response for error creation of merchant")
    public void givenInvalidMerchantRegistration_whenRegisterIndividual_thenBadRequestResponse() {

        // Given
        UserRegistration validUserRegistration = AuthUtils.invalidJsonForCreatingTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register/merchants")
                .body(Mono.just(validUserRegistration), UserRegistration.class)
                .exchange();

        // Then
        result.expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should return unauthorized when login")
    public void givenInvalidLoginRequest_whenLogin_thenReturnUnAuthorized() {
        // Given
        WebTestClient.ResponseSpec register = webTestClient.post().uri("/api/v1/auth/register/merchants")
                .body(Mono.just(AuthUtils.registrationForLoginTest()), UserRegistration.class)
                .exchange();
        LoginRequest validLoginRequest = AuthUtils.jsonForLoginInvalidTest();

        // When
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/login")
                .body(Mono.just(validLoginRequest), LoginRequest.class)
                .exchange();

        // Then
        result.expectStatus().isUnauthorized();
    }
}