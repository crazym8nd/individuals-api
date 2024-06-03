package com.crazym8nd.individualsapi.rest;


import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.service.KeycloakService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthControllerV1 {
    private final KeycloakService keycloakService;



    @PostMapping("/login")
    public Mono<AccessTokenResponse> login(@RequestBody LoginRequest request) {
        return Mono.just(keycloakService.getToken(request));
    }

    @PostMapping("/registration")
    public Mono<UserRegistration> createUser(@RequestBody UserRegistration userRegistrationRecord) {
        return Mono.just(keycloakService.createUser(userRegistrationRecord));
    }

    @GetMapping("/info/{userName}")
    public Mono<?> getUserInfo(@PathVariable String userName) {
        Mono<UserRepresentation> userInfo = Mono.just(keycloakService.getUserById(userName));
        return userInfo.flatMap(user -> {
            String logMessage = String.format("Username: %s, Email: %s",
                    user.getUsername(), user.getEmail());
            return Mono.just(logMessage);
        }).onErrorResume(error -> Mono.error(new RuntimeException("Internal server error")));
    }
}
