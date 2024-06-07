package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface KeycloakService {

    UserRegistration createUser(UserRegistration userRegistration);

    UserRepresentation getUserByUsername(String username);
    ResponseInfo getUserInfoAboutMe(String username);

    ResponseEntity<ResponseTokenLogin> getToken(LoginRequest request);
}
