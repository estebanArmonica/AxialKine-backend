package com.tiendaweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.factory.TareaCommandFactory;
import com.tiendaweb.exception.ResourceNotFoundException;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.services.ITareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tareas/")
@Tag(name = "tareas", description = "Controlador para crear actividades para cada producto")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
public class TareaController {

    private final ITareaService tareaService;
    private final IProductoRepository productoRepo;
    private final TareaCommandFactory tareaCommandFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public TareaController(ObjectMapper objectMapper, TareaCommandFactory tareaCommandFactory, ITareaService tareaService, IProductoRepository productoRepo) {
        this.tareaService = tareaService;
        this.productoRepo = productoRepo;
        this.tareaCommandFactory = tareaCommandFactory;
        this.objectMapper = objectMapper;
    }

    // listamos todas las tareas (GET)
    @GetMapping("listar")
    @Operation(
            summary = "Listado de todas las tareas disponibles",
            description = "Retorna un conjunto de todas las tareas de los productos disponibles",
            tags = {"tareas"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = Tarea.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No hay tareas para los productos registradas para listar"
                    )
            }
    )
    public ResponseEntity<List<Tarea>> obtenerTodoTarea() {
        // devuelve 200 OK
        try {
            Command<List<Tarea>> command = tareaCommandFactory.obtenerTareaCommand();
            List<Tarea> tareas = command.execute();
            return ResponseEntity.ok(tareas);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // buscamos por el id da la tarea (GET/ID)
    @GetMapping("listar/{id}")
    @Operation(
            summary = "Listar por id",
            description = "Endpoint para listar una tarea existente por id",
            tags = {"tareas"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = Tarea.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No existe el producto buscado"
                    )
            }
    )
    public ResponseEntity<?> obtenerPorIdTarea(@PathVariable("id") Long id)throws Exception, NullPointerException, FileNotFoundException {
        try {
            Command<Optional<Tarea>> command = tareaCommandFactory.buscarTareaCommand(id);
            Optional<Tarea> tareas = command.execute();
            return ResponseEntity.ok(tareas);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // creamos una tarea (POST)
    @PostMapping(value = "crear-tarea", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Registra una nueva tarea para un producto",
            description = "Retorna un json de los datos de la tarea creada",
            tags = {"tareas"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tarea creada correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Tarea.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<Tarea> agregarTarea(@Parameter(name = "tarea", description = "JSON con los datos para crear una tarea", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "{\"nombre\":\"Ejemplo\",\"prod_id\":1}"))
                                              @RequestParam("tarea") String tareaJson,
                                              @Parameter(name = "video", description = "Video para una tarea (mp4)", content = @Content(mediaType = "application/octet-stream"), schema = @Schema(type = "string", format = "binary"))
                                              @RequestParam("video")MultipartFile videoFile) throws IOException, Exception {
        try {
            Tarea tarea = objectMapper.readValue(tareaJson, Tarea.class);

            Command<Tarea> command = tareaCommandFactory.createTareaCommand(tarea, videoFile);

            Tarea tareaValida = command.execute();
            Tarea crear = tareaService.agregarTarea(tareaValida);

            return ResponseEntity.status(HttpStatus.CREATED).body(crear);
        } catch ( IOException e){
            System.out.println("Error al crear la tarea: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch ( Exception e){
            System.out.println("Error al crear la tarea: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Actualizamos una tarea (PUT/ID)
    @PutMapping(value = "update-tarea/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Actualizamos una tarea existente de un producto",
            description = "Retorna una actualizacion de la tarea buscada por el ID",
            tags = {"tareas"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tarea creada correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Tarea.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<Tarea> updateTarea(@PathVariable("id") Long id,
                                             @Parameter(name = "tarea", description = "JSON con los datos de una tarea", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "{\"nombre\":\"Ejemplo\",\"prod_id\":1}"))
                                             @RequestParam("tarea") String tareaJson,
                                             @Parameter(name = "video", description = "Video de una tarea (mp4)",content = @Content(mediaType = "application/octet-stream"), schema = @Schema(type = "string", format = "binary"))
                                             @RequestParam("video")MultipartFile videoFile) throws IOException, Exception {
        try {
            Tarea tarea = objectMapper.readValue(tareaJson, Tarea.class);

            Command<Tarea> command = tareaCommandFactory.updateTareaCommand(id, tarea, videoFile);

            Tarea actualizar = command.execute();
            return ResponseEntity.ok(actualizar);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
