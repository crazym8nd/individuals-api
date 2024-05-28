package com.crazym8nd.individualsapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class RequestUsrDto {
    @Email(message = "Invalid email format")
    @NotNull(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotNull(message = "Password is required")
    private String password;

    @Size(min = 8, message = "Confirm password must be at least 8 characters long")
    @NotNull(message = "Confirm password is required")
    private String confirmPassword;
}
