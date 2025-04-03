package com.tiendaweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaweb.models.Producto;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.services.ITareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;

@RestController
@RequestMapping("api/tareas/")
public class TareaController {

    private ITareaService tareaService;
    private IProductoRepository productoRepo;

    @Autowired
    public TareaController(ITareaService tareaService, IProductoRepository productoRepo) {
        this.tareaService = tareaService;
        this.productoRepo = productoRepo;
    }

    // listamos todas las tareas
    @GetMapping("listar")
    public ResponseEntity<?> obtenerTodoTarea() {
        // devuelve 200 OK
        return ResponseEntity.ok(tareaService.obtenerTodoTarea());
    }

    // buscamos por el id da la tarea
    @GetMapping("listar/{id}")
    public ResponseEntity<?> obtenerPorIdTarea(@PathVariable("id") Long id)throws Exception, NullPointerException, FileNotFoundException {
        return ResponseEntity.ok(tareaService.buscarPorIdTarea(id));
    }

    // creamos una tarea
    @PostMapping("crear-tarea")
    public ResponseEntity<Tarea> agregarTarea(@RequestParam("tarea") String tareaJson, @RequestParam("video")MultipartFile videoFile) throws IOException, Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Tarea tarea = objectMapper.readValue(tareaJson, Tarea.class);

            // AGREGAMOS UNA TAREA
            tarea.setNombre(tarea.getNombre());

            // guardamos la imagen en el objecto
            if (videoFile != null && !videoFile.isEmpty()) {
                Blob blob = createBlobFromMultiPartFile(videoFile);
                tarea.setVideo(blob);
            }

            // agregamos un producto
            if(tarea.getProducto() != null && tarea.getProducto().getCodigo() != 0){
                Producto producto = productoRepo.findById(tarea.getProducto().getCodigo()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                tarea.setProducto(producto);
            }

            System.out.println("Video BLOB: " + tarea.getVideo());
            System.out.println("Producto ID: " + tarea.getProducto().getCodigo());

            // guardamos los datos
            Tarea tareaNuevo = tareaService.agregarTarea(tarea);

            return ResponseEntity.status(HttpStatus.CREATED).body(tareaNuevo);
        } catch (Exception e){
            System.out.println("Error al crear la tarea: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // creamos una tarea
    @PutMapping("update-tarea/{id}")
    public ResponseEntity<Tarea> updateTarea(Long id, @RequestParam("tarea") String tareaJson, @RequestParam("video")MultipartFile videoFile) throws IOException, Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Tarea tarea = objectMapper.readValue(tareaJson, Tarea.class);

            // buscamos una tarea por el id
            Tarea tareaExistente = tareaService.buscarPorIdTarea(id).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

            // AGREGAMOS UNA TAREA
            tareaExistente.setNombre(tarea.getNombre());

            // guardamos la imagen en el objecto
            if (videoFile != null && !videoFile.isEmpty()) {
                Blob blob = createBlobFromMultiPartFile(videoFile);
                tareaExistente.setVideo(blob);
            }

            // agregamos un producto
            if(tarea.getProducto() != null && tarea.getProducto().getCodigo() != 0){
                Producto producto = productoRepo.findById(tarea.getProducto().getCodigo()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                tareaExistente.setProducto(producto);
            }

            System.out.println("Video BLOB: " + tarea.getVideo());
            System.out.println("Producto ID: " + tarea.getProducto().getCodigo());

            // guardamos los datos
            Tarea tareaActualizada = tareaService.updateTarea(id, tareaExistente);

            return ResponseEntity.status(HttpStatus.OK).body(tareaActualizada);
        } catch (Exception e){
            System.out.println("Error al crear la tarea: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private Blob createBlobFromMultiPartFile(MultipartFile file) throws Exception, IOException {
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }
}
