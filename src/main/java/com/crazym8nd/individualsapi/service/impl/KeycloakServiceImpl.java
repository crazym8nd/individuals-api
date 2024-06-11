package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidCreatingUserException;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidLoginException;
import com.crazym8nd.individualsapi.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;


    @Override
    public Mono<ResponseTokenLogin> getToken(LoginRequest request) {
        try {return Mono.just(ResponseTokenLogin.builder()
                    .accessToken(keycloak.tokenManager().getAccessToken().getToken())
                    .expiresIn(keycloak.tokenManager().getAccessToken().getExpiresIn())
                    .refreshToken(keycloak.tokenManager().getAccessToken().getRefreshToken())
                    .tokenType(keycloak.tokenManager().getAccessToken().getTokenType())
                    .build());
        } catch (Exception e) {
            return Mono.error(new InvalidLoginException(e.getMessage()));
        }

    }

    @Override
    public Mono<String> createUser(UserRegistration userRegistration, String roleName) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistration.getUsername());
        user.setEmail(userRegistration.getEmail());
        user.setFirstName(userRegistration.getFirstName());
        user.setLastName(userRegistration.getLastName());
        user.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistration.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);


        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);
        user.setCredentials(list);

        UsersResource usersResource = getUsersResource();

        Response response = usersResource.create(user);
        URI uri = response.getLocation();
        if (uri != null) {
            String createdUserId = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
            log.info("Created user {}", createdUserId);

            assignRole(createdUserId, roleName);
            return Mono.just(createdUserId);
        } else {
            return Mono.error(new InvalidCreatingUserException("Unable to create user"));
        }
    }


    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }

    @Override
    public Mono<UserResource> getUserResource(String userId){
        UsersResource usersResource = getUsersResource();
        return Mono.just(usersResource.get(userId));
    }


    @Override
    public Mono<Void> assignRole(String userId, String roleName) {
        UserResource userResource = getUserResource(userId).block();
        RolesResource rolesResource = getRolesResource();
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();

        log.info(rolesResource.get(roleName).toRepresentation().toString());
        userResource.roles().realmLevel().add(Collections.singletonList(representation));
        return Mono.empty();
    }

    private RolesResource getRolesResource(){
        return  keycloak.realm(realm).roles();
    }
}
