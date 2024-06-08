package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface KeycloakService {

    void assignRole(String userId, String roleName);

    String createUser(UserRegistration userRegistration, String roleName);

    UserRepresentation getUserByUsername(String username);
    ResponseInfo getUserInfoAboutMe(String username);
    UserResource getUserResource(String userId);

    ResponseEntity<ResponseTokenLogin> getToken(LoginRequest request);
}
