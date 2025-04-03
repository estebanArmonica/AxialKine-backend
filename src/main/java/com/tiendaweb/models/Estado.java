package com.tiendaweb.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estado")
public class Estado {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long id;

    @Column(name = "testado", nullable = false, length = 15)
    private String tipo;

    // constructores con y sin parametros
    public Estado(Long id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Estado() {}

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
