package com.crazym8nd.individualsapi.dto;

public record UserRegistration(String username,
                                     String email,
                                     String firstName,
                                     String lastName,
                                     String password) {
}
