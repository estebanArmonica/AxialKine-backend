package com.tiendaweb.models.transbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiendaweb.models.Item;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private int id;

    @JsonProperty("buy_order")
    @Column(name = "buy_order", nullable = false, length = 26)
    private String buyOrder;

    @JsonProperty("session_id")
    @Column(name = "session_id", nullable = false, length = 61)
    private String sessionId;

    @Column(nullable = false)
    private double amount;

    @JsonProperty("return_url")
    @Column(name = "return_url", nullable = false, length = 256)
    private String returnUrl;

    @JsonProperty("status")
    @Column(nullable = false, length = 20)
    private String status = "PENDIENTE";

    @Column(name = "token", length = 64)
    private String token;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "items", nullable = false)
    @JsonProperty("items")
    private Item item;

    public Compra(int id, String buyOrder, String sessionId, double amount, String returnUrl, String status, String token,  Item item) {
        this.id = id;
        this.buyOrder = buyOrder;
        this.sessionId = sessionId;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.status = "PENDIENTE";
        this.token = token;
        this.item = item;
    }

    public Compra() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("buy_order")
    public String getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(String buyOrder) {
        if (buyOrder == null || buyOrder.trim().isEmpty()) {
            throw new IllegalArgumentException("buyOrder no puede ser nulo o vacío");
        }
        if (buyOrder.length() > 26) { // Ajusta según tu definición de columna
            throw new IllegalArgumentException("buyOrder no puede exceder los 26 caracteres");
        }
        this.buyOrder = buyOrder;
    }

    @JsonProperty("session_id")
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

    @JsonProperty("return_url")
    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        if(returnUrl == null || returnUrl.trim().isEmpty()){
            throw new IllegalArgumentException("returnUrl no puede ser nulo o vacío");
        }
        this.returnUrl = returnUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status == null || status.trim().isEmpty()){
           throw new IllegalArgumentException("El estado no puede ser nulo o vacío");
        }
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
