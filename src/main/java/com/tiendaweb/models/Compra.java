package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "buy_order", nullable = false, length = 26)
    private String BuyOrder;

    @Column(name = "session_id", nullable = false, length = 61)
    private String sessionId;

    @Column(nullable = false)
    private double amount;

    @Column(name = "return_url", nullable = false, length = 256)
    private String returnUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "items", nullable = false)
    @JsonProperty("items")
    private Item item;

    public Compra(int id, String buyOrder, String sessionId, double amount, String returnUrl, Item item) {
        this.id = id;
        BuyOrder = buyOrder;
        this.sessionId = sessionId;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.item = item;
    }

    public Compra() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyOrder() {
        return BuyOrder;
    }

    public void setBuyOrder(String buyOrder) {
        BuyOrder = buyOrder;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
