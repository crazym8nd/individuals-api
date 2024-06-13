package com.crazym8nd.individualsapi.service.impl;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidLoginException;
import com.crazym8nd.individualsapi.service.AuthService;
import lombok.Setter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${keycloak.realm}")
    @Setter
    private String realm;

    @Value("${keycloak.urls.auth}")
    @Setter
    private String authServerUrl;

    @Value("${keycloak.clientId}")
    @Setter
    private String clientId;

    @Value("${keycloak.clientSecret}")
    @Setter
    private String clientSecret;


    public Keycloak keycloakForAuth(LoginRequest request){
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(request.username())
                .password(request.password())
                .build();
    }

    @Override
    public Mono<ResponseTokenLogin> getToken(LoginRequest request) {
        try{
            if (request.username() == null || request.password() == null) {
                return Mono.error(new InvalidLoginException("Invalid credentials"));
            }
           AccessTokenResponse token = keycloakForAuth(request).tokenManager().getAccessToken();

            return Mono.just(ResponseTokenLogin.builder()
                    .accessToken(token.getToken())
                    .expiresIn(token.getExpiresIn())
                    .refreshToken(token.getRefreshToken())
                    .tokenType(token.getTokenType())
                    .build());


        } catch (Exception e) {
            return Mono.error(new InvalidLoginException("Invalid credentials"));
        }
    }
}
