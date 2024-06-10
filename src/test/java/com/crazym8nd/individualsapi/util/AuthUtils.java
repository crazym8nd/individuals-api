package com.crazym8nd.individualsapi.util;

import com.crazym8nd.individualsapi.dto.request.UserRegistration;

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

}
