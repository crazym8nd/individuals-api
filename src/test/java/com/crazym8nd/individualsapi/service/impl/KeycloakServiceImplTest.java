package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidCreatingUserException;
import com.crazym8nd.individualsapi.util.AuthUtils;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Should return InvalidCreatingUserException for invalid creating of user")
    public void givenInvalidRegistrationRequest_whenRegisterUser_thenReturnInvalidCreatingUserException() {
        // Given
        UserRegistration userRegistration = AuthUtils.invalidJsonForCreatingTest();
        String role = "MERCHANTS";

        // When
        Mono<String> result = keycloakService.createUser(userRegistration, role);
        // Then
        StepVerifier.create(result)
                .expectError(InvalidCreatingUserException.class)
                .verify();
    }


}