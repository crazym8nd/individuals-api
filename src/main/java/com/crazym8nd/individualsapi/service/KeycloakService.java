package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface KeycloakService {

    UserRegistration createUser(UserRegistration userRegistration);

    UserRepresentation getUserByUsername(String username);

    ResponseEntity<AccessTokenResponse> getToken(LoginRequest request);
}
