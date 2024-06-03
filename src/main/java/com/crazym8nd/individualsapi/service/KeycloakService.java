package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface KeycloakService {

    UserRegistration createUser(UserRegistration userRegistration);
    void updateUser(String username);
    UserResource getUserResource(String userId);
    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);
    void emailVerification(String userId);
    void forgotPassword(String username);
    AccessTokenResponse getToken(LoginRequest request);
}
