package com.tiendaweb.controllers;

import com.tiendaweb.commands.handlers.EstadoCommandHandler;
import com.tiendaweb.commands.impl.estado.ListEstadoCommandImpl;
import com.tiendaweb.models.Estado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/estados/")
@Tag(name = "estados")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = RequestMethod.GET)
public class EstadoController {

    private final EstadoCommandHandler commandHandler;

    @Autowired
    public EstadoController(EstadoCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    // listamos todos los estados
    @GetMapping("listar")
    @Operation(
            summary = "Listar todos los estados",
            description = "Retorna un conjunto de todos los estados disponibles.",
            tags = {"estados"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = Estado.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No hay estados registradas"
                    )
            }
    )
    public ResponseEntity<?> obtenerTodo() {
        ListEstadoCommandImpl command = new ListEstadoCommandImpl();
        return ResponseEntity.ok(commandHandler.handle(command));
    }
}
