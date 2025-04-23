package com.tiendaweb.controllers;

import com.tiendaweb.commands.handlers.CategoryCommandHandler;
import com.tiendaweb.commands.impl.categoria.CreateCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.ListCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.ListIdCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.UpdateCategoryCommandImpl;
import com.tiendaweb.models.Categoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/categoria/")
@Tag(name = "Categoria", description = "Controlador de Categorias")
@CrossOrigin(origins = {"http://localhost:4200"},
             methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
public class CategoriaController {

    private final CategoryCommandHandler commandHandler;

    @Autowired
    public CategoriaController(CategoryCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    // listamos todas las categorias
    @GetMapping("listar")
    @Operation(
            summary = "Listar todas las categorías",
            description = "Retorna un conjunto de todas las categorías disponibles.",
            tags = {"Categoria"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = Categoria.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No hay categorías registradas"
                    )
            }
    )
    public ResponseEntity<?> obtenerTodo() {
        ListCategoryCommandImpl command = new ListCategoryCommandImpl();
        return ResponseEntity.ok(commandHandler.handle(command));
    }

    // buscamos una categoria por el ID
    @GetMapping("listar/{idCate}")
    @Operation(
            summary = "Listamos una categoría por Id",
            description = "Retonar un dato de todas las categorias disponible mediante su Id",
            tags = {"Categoria"},
            parameters = {
              @Parameter(
                      name = "idCate",
                      description = "El id es requerido para buscar una categoría",
                      example = "1",
                      required = true
              )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de la categoría exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Categoria.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Categoria no encontrada"
                    )
            }
    )
    public ResponseEntity<?> obternerPorId(@PathVariable("idCate") Long idCate){
        ListIdCategoryCommandImpl command = new ListIdCategoryCommandImpl(idCate);
        return ResponseEntity.ok(commandHandler.handle(command));
    }

    // agregamos una categoria
    @PostMapping("crear-cate")
    @Operation(
            summary = "Create a new Category",
            description = "Crea una categoria con los datos correspondiente que agrege el admin",
            tags = {"Categoria"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dato requerido: 'descripcion' (required). Example: {\"descripcion\": \\\"Electronics\\\"}",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Categoria creada con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Categoria.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error validación (e.g. campo descripcion vacío)"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict (e.g. La categoria ya existe)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<Categoria> agregarCategoria(@RequestBody Categoria cate) {
        CreateCategoryCommandImpl command = new CreateCategoryCommandImpl(cate);

        return ResponseEntity.status(HttpStatus.CREATED).body(commandHandler.handle(command));
    }

    // actualizamos una categoria
    @PutMapping("update-cate/{idCate}")
    @Operation(
            summary = "Actualizamos una Categoria por el ID",
            description = "Actualizamos un dato de la categoria requiere permisos de ADMIN",
            tags = {"Categoria"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Actualizamos un dato de una de las categorias disponibles",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Actualizada correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Categoria.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en el formato del dato"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No existe la categoria"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<Categoria> updateCategoria(@PathVariable("idCate") Long idCate, @RequestBody Categoria cate) {
        UpdateCategoryCommandImpl command = new UpdateCategoryCommandImpl(idCate, cate);

        Categoria cateActualizada = commandHandler.handle(command);
        return ResponseEntity.ok(cateActualizada);
    }
}

