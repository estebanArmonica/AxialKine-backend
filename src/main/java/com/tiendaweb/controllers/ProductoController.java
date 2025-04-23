package com.tiendaweb.controllers;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.factory.ProductoCommandFactory;
import com.tiendaweb.exception.BusinessException;
import com.tiendaweb.exception.ConcurrencyException;
import com.tiendaweb.exception.ResourceNotFoundException;
import com.tiendaweb.exception.ValidationException;
import com.tiendaweb.models.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/productos/")
@Tag(name = "productos", description = "Controlador de productos")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
public class ProductoController {

    private final ObjectMapper objectMapper;
    private final ProductoCommandFactory commandFactory;

    @Autowired
    public ProductoController(ObjectMapper objectMapper, ProductoCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        this.objectMapper = objectMapper;
    }

    // listamos todos los productos (GET)
    @GetMapping("listar")
    @Operation(
            summary = "Listar todos los productos disponibles",
            description = "Retorna un conjunto de todos los productos disponibles.",
            tags = {"productos"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = Producto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No hay productos registrados"
                    )
            }
    )
    public ResponseEntity<List<Producto>> obtenerTodo() {
        try {
            Command<List<Producto>> command = commandFactory.obtenerTodoProductoCommand();
            List<Producto> productos = command.execute();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            System.out.println("NO CONTENT: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // buscamos un producto por el ID (GET/ID)
    @GetMapping("listar/{codigo}")
    @Operation(
            summary = "Listar por id",
            description = "Endpoint para listar un producto existente por id",
            tags = {"Productos"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = Producto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No existe el producto buscado"
                    )
            }
    )
    public ResponseEntity<?> buscarPorCodigo(@PathVariable("codigo") int codigo) {
        try {
            Command<Optional<Producto>> command = commandFactory.buscarProductoCommand(codigo);
            return ResponseEntity.ok(command);
        } catch (Exception e) {
            System.out.println("BAD REQUEST: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // creamos un nuevo producto (POST)
    @PostMapping(value = "create-prod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear producto", description = "Endpoint para registrar un nuevo producto con imagen", tags = {"Productos"})
    public ResponseEntity<Producto> agregarProducto(@Parameter(name = "producto", description = "JSON con los datos del producto", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "{\"nombre\":\"Ejemplo\",\"precio\":100}"))
                                                    @RequestParam("producto") String productoJson,
                                                    @Parameter(name = "imagen", description = "Imagen del producto (JPEG, PNG)", content = @Content(mediaType = "application/octet-stream"), schema = @Schema(type = "string", format = "binary"))
                                                    @RequestParam("imagen")  MultipartFile imagenFile) throws Exception, IOException {
        try {
            Producto producto = objectMapper.readValue(productoJson, Producto.class);
            Command<Producto> createCommand = commandFactory.createProductoCommand(producto, imagenFile);

            // guardamos los datos
            Producto prodNuevo = createCommand.execute();

            return ResponseEntity.status(HttpStatus.CREATED).body(prodNuevo);
        } catch (ValidationException e) {
            System.out.println("BAD REQUEST: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            System.out.println("NOT FOUND: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ConcurrencyException e) {
            System.out.println("CONFLICT: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (IOException e) {
            System.out.println("INTERNAL SERVER ERROR (IOexception): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            System.out.println("INTERNAL SERVER ERROR (Exception): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // actualizar un producto (PUT/ID)
    @PutMapping(value = "/update-prod/{codigo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar Producto", description = "Endpoint para actualizar un producto existente", tags = {"Productos"})
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("codigo") int codigo,
                                                       @Parameter(name = "producto", description = "JSON con los datos del producto", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "{\"nombre\":\"Ejemplo\",\"precio\":100}"))
                                                       @RequestParam("producto") String productoJson,
                                                       @Parameter(name = "imagen", description = "Imagen del producto (JPEG, PNG)", content = @Content(mediaType = "application/octet-stream"), schema = @Schema(type = "string", format = "binary"))
                                                       @RequestParam("imagen") MultipartFile imagenFile) throws Exception, IOException, NullPointerException {
        try {
            Producto producto = objectMapper.readValue(productoJson, Producto.class);

            Command<Producto> updateCommand = commandFactory.updateProductoCommand(codigo, producto, imagenFile);

            // guardamos los datos del nuevo producto
            Producto productoActualizado = updateCommand.execute();

            // retornamos los datos guardados
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizado);
        } catch (ValidationException e) {
            System.err.println("BAD REQUEST: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (ResourceNotFoundException e) {
            System.err.println("NOT FOUND: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BusinessException e) {
            System.err.println("CONFLICT: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IOException e) {
            System.err.println("INTERNAL SERVER ERROR (IOException): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            System.err.println("INTERNAL SERVER ERROR (Exception): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
