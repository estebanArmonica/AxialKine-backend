package com.tiendaweb.controllers;

import com.tiendaweb.services.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/estados/")
public class EstadoController {

    private IEstadoService estadoService;

    @Autowired
    public EstadoController(IEstadoService estadoService) {
        this.estadoService = estadoService;
    }

    // listamos todos los estados
    @GetMapping("listar")
    public ResponseEntity<?> obtenerTodo() {
        return ResponseEntity.ok(estadoService.obtenerTodo());
    }
}
