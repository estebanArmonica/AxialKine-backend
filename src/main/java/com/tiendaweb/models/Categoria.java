package com.tiendaweb.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
public class Categoria {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cate")
    private Long id;

    @Column(length = 45, nullable = false)
    private String descripcion;

    // constructor con y sin parametros
    public Categoria(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Categoria() {}

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
