package com.tiendaweb.controllers;

import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IRolRepository;
import com.tiendaweb.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/usuarios/")
public class UsuarioController {
    private IRolRepository rolRepo;
    private IUsuarioRepository userRepo;

    @Autowired
    public UsuarioController(IRolRepository rolRepo, IUsuarioRepository userRepo) {
        this.userRepo = userRepo;
        this.rolRepo = rolRepo;
    }

    // listamos a todos los usuarios, independiente de su rol
    @GetMapping("listar")
    public ResponseEntity<List<Usuario>> obtenerTodo(){
        try {
            List<Usuario> usuarios = userRepo.findAll(); // obtenemos todos los usuarios

            // verificamos si la lista esta vacia
            if(usuarios.isEmpty()){
                // retorna NOT_FOUND si no hay usuarios
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            // Si hay un error de procesamiento JSON, retorna un INTERNAL_SERVER_ERROR
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // listamos todos los clientes cuyo rol solo sea "USER"
    @GetMapping("listar-clientes")
    public ResponseEntity<List<Usuario>> obtenerUsuarioPorRol(){
        try {
            List<Usuario> usuarios = userRepo.findByRolId(2L); // filtramos por el ID del usuario

            // verificamos si la lista esta vacía
            if(usuarios.isEmpty()){
                // retornamos un NOT_FOUND si no hay usuarios
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(usuarios);
        } catch (Exception ex) {
            // si hay error de procesamiento JSON, retorna un INTERNAL_SERVER_ERROR
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
