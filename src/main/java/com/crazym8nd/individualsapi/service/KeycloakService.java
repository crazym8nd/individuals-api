package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import org.keycloak.admin.client.resource.UserResource;
import reactor.core.publisher.Mono;

public interface KeycloakService {

    Mono<Void> assignRole(String userId, String roleName);

    Mono<String> createUser(UserRegistration userRegistration, String roleName);

    Mono<UserResource> getUserResource(String userId);
}
