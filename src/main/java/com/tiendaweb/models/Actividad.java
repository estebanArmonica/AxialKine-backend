package com.tiendaweb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "actividad")
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_activi")
    private int id;

    @Column(length = 25, nullable = false)
    private String realizado;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tarea_id", nullable = false)
    @JsonProperty("tarea_id")
    private Tarea tarea;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "checkeo_tarea", joinColumns = @JoinColumn(name = "actividad_id", referencedColumnName = "id_activi")
               , inverseJoinColumns = @JoinColumn(name = "checkeo_id", referencedColumnName = "id_check" ))
    private List<Checkeo> checkeos = new ArrayList<>();

    public Actividad(int id, String realizado, Tarea tarea, List<Checkeo> checkeos) {
        this.id = id;
        this.realizado = realizado;
        this.tarea = tarea;
        this.checkeos = checkeos;
    }

    public Actividad(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealizado() {
        return realizado;
    }

    public void setRealizado(String realizado) {
        this.realizado = realizado;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public List<Checkeo> getCheckeos() {
        return checkeos;
    }

    public void setCheckeos(List<Checkeo> checkeos) {
        this.checkeos = checkeos;
    }
}
