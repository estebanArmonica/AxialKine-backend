package com.tiendaweb.models.transbank;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TransbankConfirmation {
    @JsonProperty("amount")
    private double amount;

    @JsonProperty("authorization_code")
    private String authorizationCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("buy_order")
    private String buyOrder;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("card_detail")
    private CardDetail cardDetail;

    @JsonProperty("transaction_date")
    private LocalDateTime transactionDate;

    // Clase interna para detalles de tarjeta
    public static class CardDetail {
        @JsonProperty("card_number")
        private String cardNumber;

        // getters y setters
        public String getCardNumber(){
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    // Getters y Setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public CardDetail getCardDetail() {
        return cardDetail;
    }

    public void setCardDetail(CardDetail cardDetail) {
        this.cardDetail = cardDetail;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
