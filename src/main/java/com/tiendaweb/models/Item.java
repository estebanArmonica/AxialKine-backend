package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item")
public class Item {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    private int cantidad;
    private int precio;

    // relaciones
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto", nullable = true)
    @JsonProperty("producto")
    private Producto producto;

    // relaciones
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", nullable = false)
    @JsonProperty("user")
    private Usuario user;

    public Item(Long id, int cantidad, int precio, Producto producto, Usuario user) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.producto = producto;
        this.user = user;
    }

    public Item(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
