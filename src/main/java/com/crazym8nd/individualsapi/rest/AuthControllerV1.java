package com.crazym8nd.individualsapi.rest;

import com.crazym8nd.individualsapi.dto.RequestUsrDto;
import com.crazym8nd.individualsapi.dto.ResponseRegistrationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {
  //  private final UserService userService;
   // private final UserMapper userMapper;

    @PostMapping("/merchants/registration")
    public Mono<ResponseEntity<ResponseRegistrationDto>> register(@RequestBody RequestUsrDto usrDto) {
        ResponseRegistrationDto response = ResponseRegistrationDto.builder().build();
        return Mono.just(ResponseEntity.ok(response));
    }

//    @PostMapping("/login")
//    public Mono<ResponseEntity<AuthResponseDto>> login(@RequestBody RequestAuthDto authDto) {
//        return Mono<ResponseEntity<AuthResponseDto>;
//    }

    @GetMapping("/info/{userName}")
    public Mono<ResponseEntity<String>> getUsrInfo(@PathVariable String userName) {
        String response = "Hello" + userName;
        return Mono.just(ResponseEntity.ok(response));
    }
}
