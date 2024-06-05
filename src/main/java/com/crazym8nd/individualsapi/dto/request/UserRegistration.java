package com.crazym8nd.individualsapi.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class UserRegistration {
   private String username;
    private  String email;
    private String firstName;
    private  String lastName;
    private  String password;
}
