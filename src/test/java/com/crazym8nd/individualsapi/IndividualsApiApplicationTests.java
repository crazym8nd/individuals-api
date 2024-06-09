package com.crazym8nd.individualsapi;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndividualsApiApplicationTests {

 @Container
 KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4")
         .withEnv("DB_VENDOR", "h2")
         .withEnv("DB_URL", "jdbc:h2:mem:testdb")
         .withEnv("DB_USER", "sa")
         .withEnv("DB_PASSWORD", "")
         .withRealmImportFile("config/keycloak/import/realm-export.json");


 @BeforeEach
 void setUp() {
  keycloak = new KeycloakContainer()
          .withEnv("DB_VENDOR", "h2")
          .withEnv("DB_URL", "jdbc:h2:mem:testdb")
          .withEnv("DB_USER", "sa")
          .withEnv("DB_PASSWORD", "")
          .withRealmImportFile("realm-export.json");
  keycloak.start();
 }

 @Test
 @DisplayName("Load context")
 void loadContext() {
  Keycloak adminClient = KeycloakBuilder.builder()
          .serverUrl(keycloak.getAuthServerUrl())
          .realm("master")
          .clientId(KeycloakContainer.ADMIN_CLI_CLIENT)
          .username(keycloak.getAdminUsername())
          .password(keycloak.getAdminPassword())
          .build();


 }






}
