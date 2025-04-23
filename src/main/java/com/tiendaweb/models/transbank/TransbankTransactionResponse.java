package com.tiendaweb.models.transbank;

public class TransbankTransactionResponse {
    private String token;
    private String url;

    public TransbankTransactionResponse(String token, String url) {
        this.token = token;
        this.url = url;
    }

    public TransbankTransactionResponse(){}

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

    // Método para actualizar la URL con el token
    public void setUrlWithToken(String url) {
        this.url = url + "?token_ws=" + this.token;
    }
}
