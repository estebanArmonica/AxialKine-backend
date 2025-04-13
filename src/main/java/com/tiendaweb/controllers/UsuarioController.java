package com.tiendaweb.controllers;

import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IRolRepository;
import com.tiendaweb.repositories.IUsuarioRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
            List<Usuario> usuarios = userRepo.findByRolId(2L); // filtramos por el ID del usuario

            // configuración del archivo excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Clientes");

            // creamos el encabezado
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("RUT");
            header.createCell(2).setCellValue("DV");
            header.createCell(3).setCellValue("NOMBRES");
            header.createCell(4).setCellValue("APELLIDOS");
            header.createCell(5).setCellValue("EMAIL");
            header.createCell(6).setCellValue("USERNAME");

            // llenamos los datos en el excel
            int rowNum = 1;
            for(Usuario usuario: usuarios){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(usuario.getId());
                row.createCell(1).setCellValue(usuario.getRut());
                row.createCell(2).setCellValue(usuario.getDv());
                row.createCell(3).setCellValue(usuario.getNombre());
                row.createCell(4).setCellValue(usuario.getApellido());
                row.createCell(5).setCellValue(usuario.getEmail());
                row.createCell(6).setCellValue(usuario.getUsername());
            }

            // ESPECIFICAMOS EL TIPO DE CONTENIDO
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=clientes.xlsx");

            // CERRAMOS EL ARCHIVO
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception ex) {
            // si hay error de procesamiento JSON, retorna un INTERNAL_SERVER_ERROR
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
