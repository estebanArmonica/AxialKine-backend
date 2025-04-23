package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiendaweb.models.transbank.Compra;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_det_compra", unique = true)
    private int id;

    @Column(name = "fech_det_compra", nullable = false)
    private LocalDate fechaDetCompra;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orden_compra", nullable = false)
    @JsonProperty("orden_compra")
    private Compra compra;

    // constructores
    public DetalleCompra(int id, LocalDate fechaDetCompra, Compra compra) {
        this.id = id;
        this.fechaDetCompra = fechaDetCompra;
        this.compra = compra;
    }

    public DetalleCompra(){}

    // getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaDetCompra() {
        return fechaDetCompra;
    }

    public void setFechaDetCompra(LocalDate fechaDetCompra) {
        this.fechaDetCompra = fechaDetCompra;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
}
