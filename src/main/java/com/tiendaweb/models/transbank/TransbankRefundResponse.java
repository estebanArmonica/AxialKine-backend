package com.tiendaweb.models.transbank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransbankRefundResponse {
    @JsonProperty("type")
    private String type;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("authorization_code")
    private String authorizationCode;

    @JsonProperty("response_code")
    private int responseCode;

    @JsonProperty("nullified_amount")
    private double nullifiedAmount;

    // Getters y Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public double getNullifiedAmount() {
        return nullifiedAmount;
    }

    public void setNullifiedAmount(double nullifiedAmount) {
        this.nullifiedAmount = nullifiedAmount;
    }
}
