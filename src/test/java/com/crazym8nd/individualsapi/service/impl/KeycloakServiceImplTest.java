package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.util.AuthUtils;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class KeycloakServiceImplTest {

    Keycloak keycloakMock = mock(Keycloak.class);
    KeycloakServiceImpl keycloakService = new KeycloakServiceImpl(keycloakMock);

    @Test
    public void test_valid_credentials_return_correct_access_token() {
        // Given
        LoginRequest request = new LoginRequest("validUser", "validPassword");
        AccessTokenResponse expectedToken = AuthUtils.fakeTokenForGetTokenTest();
        TokenManager managerMock = mock(TokenManager.class);
        when(keycloakMock.tokenManager()).thenReturn(managerMock);
        when(keycloakMock.tokenManager().getAccessToken()).thenReturn(expectedToken);


        // When
        ResponseTokenLogin response = keycloakService.getToken(request).block();

        // Then
        assertNotNull(response);
        assertEquals(expectedToken.getToken(), response.getAccessToken());
        assertEquals(expectedToken.getExpiresIn(), response.getExpiresIn());
        assertEquals(expectedToken.getRefreshToken(), response.getRefreshToken());
        assertEquals(expectedToken.getTokenType(), response.getTokenType());

    }
}