package com.crazym8nd.individualsapi.rest;


import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.exceptionhandling.InvalidCreatingUserException;
import com.crazym8nd.individualsapi.service.AuthService;
import com.crazym8nd.individualsapi.service.KeycloakService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthControllerV1 {
    private final KeycloakService keycloakService;
    private final AuthService authService;



    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseTokenLogin>> login(@Valid @RequestBody LoginRequest request) {
        return authService.getToken(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/register/merchants")
    public Mono<ResponseEntity<ResponseRegistration>> createMerchant(@Valid @RequestBody UserRegistration userRegistration) {
        return Mono.defer(() -> keycloakService.createUser(userRegistration, "MERCHANTS"))
                .map(registeredUser -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ResponseRegistration.builder()
                                .username(userRegistration.getUsername())
                                .email(userRegistration.getEmail())
                                .message("Merchant successfully registered!")
                                .build()))
                .onErrorMap(ex -> new InvalidCreatingUserException(ex.getMessage()));
    }

    @PostMapping("/register/individuals")
    public Mono<ResponseEntity<ResponseRegistration>> createIndividual(@RequestBody UserRegistration userRegistration) {
        return Mono.defer(() -> keycloakService.createUser(userRegistration, "INDIVIDUALS"))
                .map(registeredUser -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ResponseRegistration.builder()
                                .username(userRegistration.getUsername())
                                .email(userRegistration.getEmail())
                                .message("Individual successfully registered!")
                                .build()))
                .onErrorMap(ex -> new InvalidCreatingUserException(ex.getMessage()));
    }

    @GetMapping("/info/me")
    public Mono<ResponseEntity<ResponseInfo>> getUserInfoAboutMe(Mono<Principal> principalMono) {
        return principalMono
                .flatMap(principal -> {
                    Authentication authentication = (Authentication) principal;
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    List<String> roleList = authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(role -> role.equals("ROLE_MERCHANTS") || role.equals("ROLE_INDIVIDUALS"))
                            .toList();
                    return Mono.just(ResponseInfo.builder()
                                    .username(principal.getName())
                                    .role(roleList)
                            .build());
                })
                .map(ResponseEntity::ok);
    }

}
