package com.tiendaweb.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UsuarioDTO {
    private String rut;
    private String dv;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String password;
    private LocalDate lastLogin;
    private MultipartFile imagen;
}
