package com.crazym8nd.individualsapi.dto.request;

public record UserRegistration(String username,
                                     String email,
                                     String firstName,
                                     String lastName,
                                     String password) {
}
