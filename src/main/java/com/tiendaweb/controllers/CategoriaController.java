package com.tiendaweb.controllers;

import com.tiendaweb.models.Categoria;
import com.tiendaweb.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categoria/")
public class CategoriaController {

    private ICategoriaService cateService;

    @Autowired
    public CategoriaController(ICategoriaService cateService) {
        this.cateService = cateService;
    }

    // listamos todas las categorias
    @GetMapping("listar")
    public ResponseEntity<?> obtenerTodo() {
        return ResponseEntity.ok(cateService.obtenerTodo());
    }

    // buscamos una categoria por el ID
    @GetMapping("listar/{idCate}")
    public ResponseEntity<?> obternerPorId(@PathVariable("idCate") Long idCate){
        return ResponseEntity.ok(cateService.obtenerIdCategoria(idCate));
    }

    // agregamos una categoria
    @PostMapping("crear-cate")
    public ResponseEntity<Categoria> agregarCategoria(@RequestBody Categoria cate) {
        Categoria categorias = cateService.agregarCategoria(cate);
        return ResponseEntity.ok(categorias);
    }

    // actualizamos una categoria
    @PutMapping("update-cate/{idCate}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable("idCate") Long idCate, @RequestBody Categoria cate) {
        try {
            String descripcion = cate.getDescripcion();
            Categoria categorias = cateService.updateCategoria(idCate, descripcion);

            return ResponseEntity.ok(categorias);
        } catch (DataIntegrityViolationException divex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

