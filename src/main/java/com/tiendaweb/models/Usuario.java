package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(length = 8, nullable = false)
    private String rut;

    @Column(length = 1, nullable = false)
    private String dv;

    @Column(length = 30, nullable = false)
    private String nombre;

    @Column(length = 30, nullable = false)
    private String apellido;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "last_login", nullable = true)
    private LocalDate lastLogin;

    @Lob
    private Blob imagen;

    // relacion entre rol y usuario "creando una tabla intermedia llamada usuarios_roles
    // el cual tendra registro y permiso de los usuarios disponibles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id_user")
            , inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id_rol"))
    private List<Rol> rol = new ArrayList<>();

    // constructores con y sin parametros
    public Usuario(Long id, String rut, String dv, String nombre, String apellido, String email, String username, String password, LocalDate lastLogin, Blob imagen, List<Rol> rol) {
        this.id = id;
        this.rut = rut;
        this.dv = dv;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
        this.lastLogin = lastLogin;
        this.imagen = imagen;
        this.rol = rol;
    }

    public Usuario(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        if(rut == null || rut.length() != 8){
            throw new IllegalArgumentException("RUT debe tener exactamente 8 caracteres");
        }
        this.rut = rut;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email == null || !email.contains("@")){
            throw new IllegalArgumentException("Email no válido");
        }
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null){
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if(password.length() < 4 || password.length() > 100){
            throw new IllegalArgumentException("La contraseñ debe tener un minimo de 4 caracteres para ser válido");
        }
        this.password = password;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public List<Rol> getRol() {
        return rol;
    }

    public void setRol(List<Rol> rol) {
        this.rol = rol;
    }

    // metodo para transformar de Blob de java.sql.Blob a byte[]
    @JsonProperty("imagen")
    public byte[] getImagenBytes() throws SQLException {
        return (imagen != null) ? imagen.getBytes(1, (int) imagen.length()): null;
    }
}