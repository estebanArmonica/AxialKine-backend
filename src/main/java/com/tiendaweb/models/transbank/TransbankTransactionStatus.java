package com.tiendaweb.models.transbank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransbankTransactionStatus {
    private String vci;
    private double amount;
    private String status;

    @JsonProperty("buy_order")
    private String buyOrder;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("accounting_date")
    private String accountingDate;

    @JsonProperty("transaction_date")
    private String transactionDate;

    @JsonProperty("authorization_code")
    private String authorizationCode;

    @JsonProperty("payment_type_code")
    private String paymentTypeCode;

    @JsonProperty("response_code")
    private byte responseCode;

    @JsonProperty("installments_amount")
    private byte installmentsAmount;

    @JsonProperty("installments_number")
    private byte installmentsNumber;

    // Clase interna para detalles de tarjeta
    public static class CardDetail {
        @JsonProperty("card_number")
        private String cardNumber;

        // Getters y Setters
        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    public TransbankTransactionStatus(String vci, double amount, String status, String buyOrder, String sessionId, String accountingDate, String transactionDate, String authorizationCode, String paymentTypeCode, byte responseCode, byte installmentsAmount, byte installmentsNumber) {
        this.vci = vci;
        this.amount = amount;
        this.status = status;
        this.buyOrder = buyOrder;
        this.sessionId = sessionId;
        this.accountingDate = accountingDate;
        this.transactionDate = transactionDate;
        this.authorizationCode = authorizationCode;
        this.paymentTypeCode = paymentTypeCode;
        this.responseCode = responseCode;
        this.installmentsAmount = installmentsAmount;
        this.installmentsNumber = installmentsNumber;
    }

    public TransbankTransactionStatus(){}

    public String getVci() {
        return vci;
    }

    public void setVci(String vci) {
        this.vci = vci;
    }

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


    public String getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public byte getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(byte responseCode) {
        this.responseCode = responseCode;
    }

    public byte getInstallmentsAmount() {
        return installmentsAmount;
    }

    public void setInstallmentsAmount(byte installmentsAmount) {
        this.installmentsAmount = installmentsAmount;
    }

    public byte getInstallmentsNumber() {
        return installmentsNumber;
    }

    public void setInstallmentsNumber(byte installmentsNumber) {
        this.installmentsNumber = installmentsNumber;
    }
}
