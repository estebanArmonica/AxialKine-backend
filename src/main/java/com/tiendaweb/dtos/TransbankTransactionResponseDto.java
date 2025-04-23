package com.tiendaweb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransbankTransactionResponseDto {
    @JsonProperty("token")
    private String token;

    @JsonProperty("url")
    private String url;

    // constructores
    public TransbankTransactionResponseDto(String token, String url) {
        this.token = token;
        this.url = url;
    }

    public TransbankTransactionResponseDto(){}

    // getter and setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
