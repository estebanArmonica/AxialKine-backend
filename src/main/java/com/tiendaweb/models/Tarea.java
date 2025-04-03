package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;
import java.sql.SQLException;

@Data
@Entity
@Table(name = "tarea")
public class Tarea {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long id;

    private String nombre;

    @Lob
    private Blob video;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prod_id", nullable = false)
    @JsonProperty("prod_id")
    private Producto producto;

    // constructor con y sin parametros
    public Tarea(Long id, String nombre, Blob video, Producto producto) {
        this.id = id;
        this.nombre = nombre;
        this.video = video;
        this.producto = producto;
    }

    public Tarea(){}

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Blob getVideo() {
        return video;
    }

    public void setVideo(Blob video) {
        this.video = video;
    }

    public Producto getProducto(){
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // metodo para transformar de Blob de java.sql.Blob a byte[]
    @JsonProperty("video")
    public byte[] getVideoBytes() throws SQLException {
        return (video != null) ? video.getBytes(1, (int) video.length()): null;
    }
}
