package com.tiendaweb.models.transbank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransbankTransactionRequest {
    @JsonProperty("buy_order")
    private String buyOrder;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("return_url")
    private String returnUrl;

    public TransbankTransactionRequest(String buyOrder, String sessionId, double amount, String returnUrl) {
        this.buyOrder = buyOrder;
        this.sessionId = sessionId;
        this.amount = amount;
        this.returnUrl = returnUrl;
    }

    public TransbankTransactionRequest(){}

    public String getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(String buyOrder) {
        this.buyOrder = buyOrder;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
