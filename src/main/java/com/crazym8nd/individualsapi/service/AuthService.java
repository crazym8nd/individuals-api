package com.crazym8nd.individualsapi.service;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.response.ResponseTokenLogin;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<ResponseTokenLogin> getToken(LoginRequest request);
}
