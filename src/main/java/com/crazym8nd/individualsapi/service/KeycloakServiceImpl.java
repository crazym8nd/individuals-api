package com.crazym8nd.individualsapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;

@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {
    private final Keycloak keycloak;
    @Override
    public RealmResource getRealmsResource() {
        return keycloak.realm("merchantsrealm");
    }
@Override
    public String getToken(){
        log.warn(keycloak.tokenManager().getAccessTokenString());
        return keycloak.tokenManager().getAccessTokenString();
    }
}
