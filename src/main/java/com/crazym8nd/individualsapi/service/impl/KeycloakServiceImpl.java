package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    @Value("${keycloak.urls.auth}")
    private String authServerUrl;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    public KeycloakServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public ResponseEntity<ResponseTokenLogin> getToken(LoginRequest request) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(request.username())
                .password(request.password())
                .build();

        ResponseEntity.ok(keycloak.tokenManager().getAccessToken());
        return ResponseEntity.ok(ResponseTokenLogin.builder()
                        .accessToken(keycloak.tokenManager().getAccessToken().getToken())
                        .expiresIn(keycloak.tokenManager().getAccessToken().getExpiresIn())
                        .refreshToken(keycloak.tokenManager().getAccessToken().getRefreshToken())
                        .tokenType(keycloak.tokenManager().getAccessToken().getTokenType())
                .build());

    }

    @Override
    public String createUser(UserRegistration userRegistration, String roleName) {
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
        String createdUserId = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
        log.info("Created user {}", createdUserId);

        assignRole(createdUserId,roleName);
        return createdUserId;
    }


    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }


    @Override
    public UserRepresentation getUserByUsername(String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(username, true);
        UserRepresentation user = users.get(0);

        return user;
    }

    @Override
    public ResponseInfo getUserInfoAboutMe(String username) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.search(username, true);
        UserRepresentation user = users.get(0);
        RoleMappingResource roleMappingResource = usersResource.get(user.getId()).roles();
        Set<RoleRepresentation> realmRoles = roleMappingResource.realmLevel().listEffective().stream().collect(Collectors.toSet());
        return ResponseInfo.builder()
                .username(user.getUsername())
                .role(null)
                .build();
    }

    @Override
    public UserResource getUserResource(String userId){
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    private ResponseInfo getUserInfoAboutMe2(String username) {
        RealmResource realmForRole = keycloak.realm(realm);
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(username, true);
        UserRepresentation user = users.get(0);
        log.info("User {} found successfully", username);
        RoleRepresentation role = realmForRole.rolesById().getRole("1c9e03f4-ce49-4976-93a6-63732bf723fd");
        return ResponseInfo.builder()
                .username(user.getUsername())
                .role(null)
                .build();
    }

    @Override
    public void assignRole(String userId, String roleName) {

        UserResource userResource = getUserResource(userId);
        RolesResource rolesResource = getRolesResource();
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();

        log.info(rolesResource.get(roleName).toRepresentation().toString());
        userResource.roles().realmLevel().add(Collections.singletonList(representation));

    }

    private RolesResource getRolesResource(){
        return  keycloak.realm(realm).roles();
    }
}
