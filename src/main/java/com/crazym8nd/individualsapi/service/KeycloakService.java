package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface KeycloakService {

    Mono<Void> assignRole(String userId, String roleName);

    Mono<String> createUser(UserRegistration userRegistration, String roleName);

    Mono<UserRepresentation> getUserByUsername(String username);
    Mono<ResponseInfo> getUserInfoAboutMe(String username);
    Mono<UserResource> getUserResource(String userId);

    Mono<ResponseEntity<ResponseTokenLogin>> getToken(LoginRequest request);
}
