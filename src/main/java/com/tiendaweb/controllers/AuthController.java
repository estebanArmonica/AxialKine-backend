package com.tiendaweb.controllers;


import com.tiendaweb.dtos.AuthRespuestaDto;
import com.tiendaweb.dtos.LoginDto;
import com.tiendaweb.dtos.RegistroDto;
import com.tiendaweb.models.Rol;
import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IRolRepository;
import com.tiendaweb.repositories.IUsuarioRepository;
import com.tiendaweb.security.JwtGenerador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;

@RestController
@RequestMapping("api/auth/")
@CrossOrigin("*")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private IRolRepository rolRepo;
    private IUsuarioRepository userRepo;
    private JwtGenerador jwtGenerador;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, IRolRepository rolRepo, IUsuarioRepository userRepo, JwtGenerador jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolRepo = rolRepo;
        this.userRepo = userRepo;
        this.jwtGenerador = jwtGenerador;
    }

    // creamos un usuario normal
    @PostMapping("create-user")
    public ResponseEntity<String> crearUsuario(@ModelAttribute RegistroDto dtoRegistro) throws Exception {
        try {
            if(userRepo.existsByUsername(dtoRegistro.getUsername())){
                return ResponseEntity.status( HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"El usuario ya existe\"}"); // respuesta en json
            }

            Usuario usuario = new Usuario();
            usuario.setRut(dtoRegistro.getRut());
            usuario.setDv(dtoRegistro.getDv());
            usuario.setNombre(dtoRegistro.getNombre());
            usuario.setApellido(dtoRegistro.getApellido());
            usuario.setUsername(dtoRegistro.getUsername());
            usuario.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
            usuario.setEmail(dtoRegistro.getEmail());

            // establecemos la fecha de ultimo inicio de sesion automatico
            usuario.setLastLogin(LocalDate.now());

            // manejamos la imagen cargada
            if(dtoRegistro.getImagen() != null && !dtoRegistro.getImagen().isEmpty()){
                usuario.setImagen(createBlobFromMultipartFile(dtoRegistro.getImagen()));
            }

            // agregamos al usuario por defecto en el rol 2 "USER"
            Rol role = rolRepo.findByTipoRol("USER").get();
            usuario.setRol(Collections.singletonList(role));

            userRepo.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Usuario creado con éxito\", \"usuario\":" + usuario + "}");
        } catch (DataIntegrityViolationException dvex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Error al crear el usuario: " + dvex.getMessage() + "\"}");
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al crear el usuario: " + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // creamos un usuario normal
    @PostMapping("create-admin")
    public ResponseEntity<String> crearAdmin(@ModelAttribute RegistroDto dtoRegistro) throws Exception {
        try {
            if(userRepo.existsByUsername(dtoRegistro.getUsername())){
                return ResponseEntity.status( HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"El usuario ya existe\"}"); // respuesta en json
            }

            if (dtoRegistro.getPassword() == null || dtoRegistro.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"La contraseña no puede estar vacía\"}");
            }

            Usuario usuario = new Usuario();
            usuario.setRut(dtoRegistro.getRut());
            usuario.setDv(dtoRegistro.getDv());
            usuario.setNombre(dtoRegistro.getNombre());
            usuario.setApellido(dtoRegistro.getApellido());
            usuario.setUsername(dtoRegistro.getUsername());
            usuario.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));


            usuario.setEmail(dtoRegistro.getEmail());

            // establecemos la fecha de ultimo inicio de sesion automatico
            usuario.setLastLogin(LocalDate.now());

            // manejamos la imagen cargada
            if(dtoRegistro.getImagen() != null && !dtoRegistro.getImagen().isEmpty()){
                usuario.setImagen(createBlobFromMultipartFile(dtoRegistro.getImagen()));
            }

            // agregamos al usuario por defecto en el rol 2 "USER"
            Rol role = rolRepo.findByTipoRol("ADMIN").get();
            usuario.setRol(Collections.singletonList(role));

            userRepo.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Usuario creado con éxito\", \"usuario\":" + usuario + "}");
        } catch (DataIntegrityViolationException dvex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Error al crear el usuario: " + dvex.getMessage() + "\"}");
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al crear el usuario: " + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Blob createBlobFromMultipartFile(MultipartFile file) throws SQLException, IOException {
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }

    // authentificacion
    @PostMapping("login")
    public ResponseEntity<AuthRespuestaDto> login(@RequestBody LoginDto loginDto, HttpSession session) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerador.generarToken(authentication);
        return new ResponseEntity<>(new AuthRespuestaDto(token), HttpStatus.OK);
    }
}
