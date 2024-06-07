package com.crazym8nd.individualsapi.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class ResponseTokenLogin {
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private String tokenType;


}
