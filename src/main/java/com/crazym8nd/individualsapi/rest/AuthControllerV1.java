package com.crazym8nd.individualsapi.rest;

import com.crazym8nd.individualsapi.dto.RequestUsrDto;
import com.crazym8nd.individualsapi.dto.ResponseRegistrationDto;
import com.crazym8nd.individualsapi.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private KeycloakService keycloakService;
  //  private final UserService userService;
   // private final UserMapper userMapper;

    @PostMapping("/merchants/registration")
    public Mono<ResponseEntity<ResponseRegistrationDto>> register(@RequestBody RequestUsrDto usrDto) {
        ResponseRegistrationDto response = ResponseRegistrationDto.builder().build();
        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/login")
    public Mono<ResponseEntity<String>> login() {
       return  Mono.just(keycloakService.getToken()).map(ResponseEntity::ok);
    }

    @GetMapping("/info/{userName}")
    public Mono<ResponseEntity<String>> getUsrInfo(@PathVariable String userName) {
        String response = "Hello" + userName;
        return Mono.just(ResponseEntity.ok(response));
    }
}
