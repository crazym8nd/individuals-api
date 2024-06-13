package com.crazym8nd.individualsapi.service.impl;


import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidLoginException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


class AuthServiceImplTest {


    private final AuthServiceImpl authService = new AuthServiceImpl();




//    @Test
//    public void whenValidRequest_thenReturnToken() {
//
//        // Given
//        LoginRequest request = new LoginRequest("user", "password");
//        Keycloak keycloak = mock(Keycloak.class);
//        authService.setAuthServerUrl("http://localhost");
//        authService.setRealm("appauth");
//        authService.setClientId("app-auth-client-id");
//        authService.setClientSecret("FuXDordwvRlgaCasQ1xsqi6s9pkAuQPI");
//
//        AccessTokenResponse expectedResponse = new AccessTokenResponse();
//        expectedResponse.setToken("access_token");
//        expectedResponse.setExpiresIn(300);
//        expectedResponse.setRefreshToken("refresh_token");
//        expectedResponse.setTokenType("bearer");
//        when(keycloak.tokenManager()).thenReturn(mock(TokenManager.class));
//        when(authService.keycloakForAuth(request).tokenManager().getAccessToken()).thenReturn(expectedResponse);
//        // When
//        ResponseTokenLogin result = authService.getToken(request).block();
//
//        // Then
//                    assertEquals("access_token", result.getAccessToken());
//                    assertEquals(Integer.valueOf(300), result.getExpiresIn());
//                    assertEquals("refresh_token", result.getRefreshToken());
//                    assertEquals("bearer", result.getTokenType());
//
//    }

    @Test
    public void givenInvalidLoginRequest_whenLogin_thenThrowInvalidLoginException() {
        // Given
        LoginRequest request = new LoginRequest(null, null);

        // When
        Mono<ResponseTokenLogin> result = authService.getToken(request);

        // Then
        StepVerifier.create(result)
                .expectError(InvalidLoginException.class)
                .verify();
    }
}