package com.tiendaweb.controllers;

import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IRolRepository;
import com.tiendaweb.repositories.IUsuarioRepository;
import com.tiendaweb.strategys.service.ReportExporterService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/usuarios/")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = {RequestMethod.GET, RequestMethod.PUT})
public class UsuarioController {
    private IRolRepository rolRepo;
    private IUsuarioRepository userRepo;

    private final ReportExporterService reportExcel;

    @Autowired
    public UsuarioController(IRolRepository rolRepo, IUsuarioRepository userRepo, ReportExporterService reportExcel) {
        this.userRepo = userRepo;
        this.rolRepo = rolRepo;
        this.reportExcel = reportExcel;
    }

    // listamos a todos los usuarios, independiente de su rol
    @GetMapping("listar")
    public ResponseEntity<List<Usuario>> obtenerTodo(){
        try {
            List<Usuario> usuarios = userRepo.findAll();

            if(usuarios.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(usuarios);

        } catch(Exception ex) {
            System.out.println("Error Exception: " + ex.getMessage());
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

    // exportamos los datos del usuario en excel
    // listamos todos los clientes cuyo rol solo sea "USER"
    @GetMapping("exportar-excel")
    public void exportExcelUsuario(HttpServletResponse response) throws IOException {
        try {
           reportExcel.exportUsuarioExcel(response, 2L); // rol con id 2
        } catch (Exception ex) {
            // si hay error de procesamiento JSON, retorna un INTERNAL_SERVER_ERROR
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error al generar el reporte: " + ex.getMessage());
            log.error("Error en el exportUsuarioExcel: " + ex);
        }
    }
}
