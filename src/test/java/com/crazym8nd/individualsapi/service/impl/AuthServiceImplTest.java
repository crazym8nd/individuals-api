package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidLoginException;
import com.crazym8nd.individualsapi.service.AuthService;
import com.crazym8nd.individualsapi.util.AuthUtils;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    Keycloak keycloakMock = mock(Keycloak.class);

    private final AuthService authService = new AuthServiceImpl();




    @Test
    public void givenValidLoginRequest_whenGettingToken_thenReturnCorrectAccessToken() {

        // Given
        LoginRequest request = new LoginRequest("validUser", "validPassword");
        AccessTokenResponse expectedToken = AuthUtils.fakeTokenForGetTokenTest();
        TokenManager managerMock = mock(TokenManager.class);
        when(keycloakMock.tokenManager()).thenReturn(managerMock);
        when(keycloakMock.tokenManager().getAccessToken()).thenReturn(expectedToken);


        // When
        Mono<ResponseTokenLogin> result = authService.getToken(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(expectedToken.getToken(), response.getAccessToken());
                    assertEquals(expectedToken.getExpiresIn(), response.getExpiresIn());
                    assertEquals(expectedToken.getRefreshToken(), response.getRefreshToken());
                    assertEquals(expectedToken.getTokenType(), response.getTokenType());
                })
                .verifyComplete();
    }

    @Test
    public void givenInvalidLoginRequest_whenGettingToken_thenReturnInvalidLoginException() {

        // Given
        LoginRequest request = null;
        TokenManager managerMock = mock(TokenManager.class);
        when(keycloakMock.tokenManager()).thenReturn(managerMock);


        // When
        Mono<ResponseTokenLogin> result = authService.getToken(request);

        // Then
        StepVerifier.create(result)
                .verifyError(InvalidLoginException.class);
    }
}