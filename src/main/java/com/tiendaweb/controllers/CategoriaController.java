package com.tiendaweb.controllers;

import com.tiendaweb.commands.handlers.CategoryCommandHandler;
import com.tiendaweb.commands.impl.categoria.CreateCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.ListCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.ListIdCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.UpdateCategoryCommandImpl;
import com.tiendaweb.models.Categoria;
import com.tiendaweb.services.ICategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categoria/")
@Tag(name = "Categoria")
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
            description = "endpoint which create an new category",
            tags = {"Categoria"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Required data: 'description' (required). Example: {\"description\": \\\"Electronics\\\"}",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created a new Category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Categoria.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error (e.g. empty description field)"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict (e.g. category already exists)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error"
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
            summary = "Update a Category for ID",
            description = "endpoint which update an category, required permiss ADMIN",
            tags = {"Categoria"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update a category with the corresponding data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update Category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Categoria.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error in the data provided"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error"
                    )
            }
    )
    public ResponseEntity<Categoria> updateCategoria(@PathVariable("idCate") Long idCate, @RequestBody Categoria cate) {
        UpdateCategoryCommandImpl command = new UpdateCategoryCommandImpl(idCate, cate);

        Categoria cateActualizada = commandHandler.handle(command);
        return ResponseEntity.ok(cateActualizada);
    }
}

