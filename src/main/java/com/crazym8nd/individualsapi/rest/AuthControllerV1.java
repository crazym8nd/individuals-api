package com.crazym8nd.individualsapi.rest;


import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.service.KeycloakService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthControllerV1 {
    private final KeycloakService keycloakService;



    @PostMapping("/login")
    public Mono<ResponseEntity<AccessTokenResponse>> login(@RequestBody LoginRequest request) {
        return Mono.just(keycloakService.getToken(request));
    }

    @PostMapping("/registration")
    public Mono<UserRegistration> createUser(@RequestBody UserRegistration userRegistration) {
        return Mono.just(keycloakService.createUser(userRegistration));
    }

    @GetMapping("/info/me")
    public Mono<?> getUserInfoAboutMe(Mono<Principal> principalMono) {
        return principalMono
                .flatMap(principal -> {
                    String userName = principal.getName();
                    return Mono.just(keycloakService.getUserByUsername(userName));
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please login to access this information."))));
    }

}
