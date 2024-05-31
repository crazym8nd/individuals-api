package com.crazym8nd.individualsapi.service;

import org.keycloak.admin.client.resource.RealmResource;

public interface KeycloakService {

    RealmResource getRealmsResource();
    String getToken();
}
