package com.crazym8nd.individualsapi.util;

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
}
