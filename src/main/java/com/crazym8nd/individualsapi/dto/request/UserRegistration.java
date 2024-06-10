package com.crazym8nd.individualsapi.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class UserRegistration {

    @NotNull(message = "Username is required")
   private String username;

    @NotNull(message = "Email is required")
    @Email
    private  String email;

    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name  is required")
    private  String lastName;

    @NotNull(message = "Password is required")
    private  String password;
}
