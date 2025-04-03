package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "producto")
public class Producto {
    //atributo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private int codigo;

    @Column(length = 255, nullable = false)
    private String nombre;

    @Column(length = 11, nullable = false)
    private int precio;

    @Lob
    private Blob imagen;

    @Column(name = "fech_creado", nullable = false)
    @JsonProperty("fech_creado")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creado;

    @Column(name = "fech_termino", nullable = false)
    @JsonProperty("fech_termino")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date terminado;

    @Column(length = 255, nullable = true)
    private String descripcion;

    // relaciones
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cate_id", nullable = false)
    @JsonProperty("cate_id")
    private Categoria cate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "status", joinColumns = @JoinColumn(name = "producto_id", referencedColumnName = "codigo")
            , inverseJoinColumns = @JoinColumn(name = "estado_id", referencedColumnName = "id_estado"))
    private List<Estado> estado = new ArrayList<>();

    @Version
    private Long version;

    // constructor con y sin parametros
    public Producto(int codigo, String nombre, int precio, Blob imagen, Date creado, Date terminado, String descripcion, Categoria cate, List<Estado> estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.creado = creado;
        this.terminado = terminado;
        this.descripcion = descripcion;
        this.cate = cate;
        this.estado = estado;
    }

    public Producto(){}

    // getter and setter
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public Date getCreado() {
        return creado;
    }

    public void setCreado(Date creado) {
        this.creado = creado;
    }

    public Date getTerminado() {
        return terminado;
    }

    public void setTerminado(Date terminado) {
        this.terminado = terminado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCate() {
        return cate;
    }

    public void setCate(Categoria cate) {
        this.cate = cate;
    }

    public List<Estado> getEstado() {
        return estado;
    }

    public void setEstado(List<Estado> estado) {
        this.estado = estado;
    }

    // metodo para transformar de Blob de java.sql.Blob a byte[]
    @JsonProperty("imagen")
    public byte[] getImagenBytes() throws SQLException {
        return (imagen != null) ? imagen.getBytes(1, (int) imagen.length()): null;
    }
}
