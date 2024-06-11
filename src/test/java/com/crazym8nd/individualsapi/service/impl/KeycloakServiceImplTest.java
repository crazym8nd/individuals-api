package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidLoginException;
import com.crazym8nd.individualsapi.service.KeycloakService;
import com.crazym8nd.individualsapi.util.AuthUtils;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class KeycloakServiceImplTest {


    private final Keycloak keycloakMock = mock(Keycloak.class);

    private final KeycloakService keycloakService = new KeycloakServiceImpl(keycloakMock);

    @Test
    public void givenValidLoginRequest_whenGettingToken_thenReturnCorrectAccessToken() {

        // Given
        LoginRequest request = new LoginRequest("validUser", "validPassword");
        AccessTokenResponse expectedToken = AuthUtils.fakeTokenForGetTokenTest();
        TokenManager managerMock = mock(TokenManager.class);
        when(keycloakMock.tokenManager()).thenReturn(managerMock);
        when(keycloakMock.tokenManager().getAccessToken()).thenReturn(expectedToken);


        // When
        Mono<ResponseTokenLogin> result = keycloakService.getToken(request);

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
        AccessTokenResponse expectedToken = AuthUtils.fakeTokenForGetTokenTest();
        TokenManager managerMock = mock(TokenManager.class);
        when(keycloakMock.tokenManager()).thenReturn(managerMock);


        // When
        Mono<ResponseTokenLogin> result = keycloakService.getToken(request);

        // Then
        StepVerifier.create(result)
                .verifyError(InvalidLoginException.class);
    }

    @Test
    public void givenValidRegistrationRequest_whenRegisterUser_thenReturnUserId() {
        // Given
        UserRegistration userRegistration = AuthUtils.validUserRegistrationForRegistrationTest();
        UsersResource mockUserResource = mock(UsersResource.class);
        RealmResource mockRealmResource = mock(RealmResource.class);
        String role = "MERCHANTS";
        Response mockResponse = mock(Response.class);
        URI userIdUri = URI.create("validUserId");

        when(mockResponse.getStatus()).thenReturn(201);
        when(mockResponse.getLocation()).thenReturn(userIdUri);
        when(keycloakMock.realm(anyString())).thenReturn(mockRealmResource);
        when(mockRealmResource.users()).thenReturn(mockUserResource);
        when(mockUserResource.create(any(UserRepresentation.class))).thenReturn(mockResponse);


        // When
        Mono<String> result = keycloakService.createUser(userRegistration, role);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(userIdUri.toString(), response);
                })
                .verifyComplete();
    }
}