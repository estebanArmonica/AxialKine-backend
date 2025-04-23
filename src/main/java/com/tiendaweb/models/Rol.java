package com.tiendaweb.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Rol {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    @Column(name = "tipo_rol", nullable = false, length = 15)
    private String tipoRol;

    // constructor con parametros y sin parametros
    public Rol(Long id, String tipoRol) {
        this.id = id;
        this.tipoRol = tipoRol;
    }

    public Rol(){}

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(String tipoRol) {
        this.tipoRol = tipoRol;
    }
}