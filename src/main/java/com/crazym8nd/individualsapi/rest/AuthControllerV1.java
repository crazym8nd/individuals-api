package com.crazym8nd.individualsapi.rest;


import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseInfo;
import com.crazym8nd.individualsapi.dto.response.ResponseRegistration;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import com.crazym8nd.individualsapi.service.KeycloakService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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



    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseTokenLogin>> login(@RequestBody LoginRequest request) {
        return keycloakService.getToken(request);
    }

    @PostMapping("/register/merchants")
    public Mono<ResponseEntity<ResponseRegistration>> createMerchant(@RequestBody UserRegistration userRegistration) {
        return Mono.just(keycloakService.createUser(userRegistration, "MERCHANTS"))
                .map(registeredUser -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ResponseRegistration.builder()
                                .username(userRegistration.getUsername())
                                .email(userRegistration.getEmail())
                                .message("Merchant successfully registered!")
                                .build()));
    }

    @PostMapping("/register/individuals")
    public Mono<ResponseEntity<ResponseRegistration>> createIndividual(@RequestBody UserRegistration userRegistration) {
        return Mono.just(keycloakService.createUser(userRegistration, "INDIVIDUALS"))
                .map(registeredUser -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ResponseRegistration.builder()
                                .username(userRegistration.getUsername())
                                .email(userRegistration.getEmail())
                                .message("Individual successfully registered!")
                                .build()));
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
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please login to access this information."))));
    }

}
