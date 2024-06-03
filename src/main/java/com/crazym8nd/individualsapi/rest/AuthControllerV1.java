package com.crazym8nd.individualsapi.rest;


import com.crazym8nd.individualsapi.dto.UserRegistration;
import com.crazym8nd.individualsapi.service.KeycloakService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class AuthControllerV1 {
    private final KeycloakService keycloakService;

    @PostMapping
    public Mono<UserRegistration> createUser(@RequestBody UserRegistration userRegistrationRecord) {
        return Mono.just(keycloakService.createUser(userRegistrationRecord));
    }

}
