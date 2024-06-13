package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidLoginException;
import com.crazym8nd.individualsapi.service.AuthService;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.urls.auth}")
    private String authServerUrl;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;


    @Override
    public Mono<ResponseTokenLogin> getToken(LoginRequest request) {
        try{
            if (request.username() == null || request.password() == null) {
                return Mono.error(new InvalidLoginException("Invalid credentials"));
            }
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(request.username())
                    .password(request.password())
                    .build();
            return Mono.just(ResponseTokenLogin.builder()
                    .accessToken(keycloak.tokenManager().getAccessToken().getToken())
                    .expiresIn(keycloak.tokenManager().getAccessToken().getExpiresIn())
                    .refreshToken(keycloak.tokenManager().getAccessToken().getRefreshToken())
                    .tokenType(keycloak.tokenManager().getAccessToken().getTokenType())
                    .build());


        } catch (Exception e) {
            return Mono.error(new InvalidLoginException("Invalid credentials"));
        }
    }
}
