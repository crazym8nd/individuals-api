package com.crazym8nd.individualsapi.util;

import com.crazym8nd.individualsapi.dto.request.LoginRequest;
import com.crazym8nd.individualsapi.dto.request.UserRegistration;
import org.keycloak.representations.AccessTokenResponse;

public class AuthUtils {

    public static UserRegistration jsonForCreatingMerchantTest(){
        return UserRegistration.builder()
                .username("merchantCreationTest")
                .email("mailmerchant@mail.com")
                .password("test")
                .firstName("testMerchant")
                .lastName("testLastname")
                .build();
    }

    public static UserRegistration jsonForCreatingIndividualTest(){
        return UserRegistration.builder()
                .username("individualCreationTest")
                .email("mailindividual@mail.com")
                .password("testtest")
                .firstName("testindividual")
                .lastName("testindividual")
                .build();
    }

    public static UserRegistration invalidJsonForCreatingTest(){
        return UserRegistration.builder()
                .password("testtest")
                .firstName("testinvalid")
                .lastName("testinvalid")
                .build();
    }

    public static AccessTokenResponse fakeTokenForGetTokenTest(){
        AccessTokenResponse fakeToken = new AccessTokenResponse();
        fakeToken.setToken("fakeToken");
        fakeToken.setExpiresIn(3600);
        fakeToken.setRefreshToken("refreshToken");
        fakeToken.setTokenType("Bearer");
        return fakeToken;
    }

    public static UserRegistration validUserRegistrationForRegistrationTest(){
        return UserRegistration.builder()
                .username("validUsername")
                .email("validemail@mail.com")
                .firstName("validFirstName")
                .lastName("validLastName")
                .password("password")
                .build();
    }

    public static LoginRequest jsonForLoginTest(){
        return new LoginRequest("test", "test");
    }
    public static LoginRequest jsonForLoginInvalidTest(){
        return new LoginRequest("username", "password");
    }

    public static UserRegistration registrationForLoginTest(){
        return UserRegistration.builder()
                .username("test")
                .email("test@mail.com")
                .password("test")
                .firstName("testMlogin")
                .lastName("testLogin")
                .build();
    }
}
