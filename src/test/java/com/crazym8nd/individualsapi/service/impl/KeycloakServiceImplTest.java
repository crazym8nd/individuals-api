package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.util.AuthUtils;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class KeycloakServiceImplTest {
    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @InjectMocks
    private KeycloakServiceImpl keycloakService;





    @Test
    public void givenValidRegistrationRequest_whenRegisterUser_thenReturnUserId() {
        // Given
        when(keycloak.realm("appauth")).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        UserRegistration userRegistration = AuthUtils.validUserRegistrationForRegistrationTest();
        String role = "MERCHANTS";

        Response mockResponse = mock(Response.class);
        URI userIdUri = URI.create("validUserId");
        when(mockResponse.getStatus()).thenReturn(201);
        when(mockResponse.getLocation()).thenReturn(userIdUri);

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