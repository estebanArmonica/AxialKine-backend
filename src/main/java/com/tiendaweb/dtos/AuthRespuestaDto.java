package com.tiendaweb.dtos;

import lombok.Data;

@Data
public class AuthRespuestaDto {
    private String accessToken;
    private String token = "Bearer ";

    public AuthRespuestaDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
