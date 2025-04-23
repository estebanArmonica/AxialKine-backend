package com.tiendaweb.controllers;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.factory.ItemCommandFactory;
import com.tiendaweb.models.Item;
import com.tiendaweb.services.IItemService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/items/")
@Tag(name = "items", description = "Controlador de items de cada usuario para compras")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
public class ItemController {

    private final ItemCommandFactory itemCommandFactory;
    private final IItemService itemService;

    @Autowired
    public ItemController(ItemCommandFactory itemCommandFactory, IItemService itemService) {
        this.itemCommandFactory = itemCommandFactory;
        this.itemService = itemService;
    }

    // listamo todo del item de un usuario
    @GetMapping("listar")
    @Operation(
            summary = "Listado de todos los items de un usuario",
            description = "Retorna un conjunto de todos los items que esten agregados",
            tags = {"items"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de los items con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = Item.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No hay items para listar"
                    )
            }
    )
    public ResponseEntity<List<Item>> obtenerTodoLista() {
        try {
            Command<List<Item>> command = itemCommandFactory.createListItemsCommand();
            List<Item> items = command.execute();
            return ResponseEntity.ok(items);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // listamos solo un item del producto por usuario
    @GetMapping("listar/{id}")
    @Operation(
            summary = "Listado los items de un usuario mediante el id",
            description = "Retorna el item agregado de un usuario",
            tags = {"items"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "El id es requerido para buscar uno de los item que tenga un usuario",
                            example = "1",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado del item con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = Item.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No hay items para listar"
                    )
            }
    )
    public ResponseEntity<?> obtenerPorIdItem(@PathVariable("id") Long id) {
        try {
            Command<Optional<Item>> command = itemCommandFactory.createListItemByIdCommand(id);
            return ResponseEntity.ok(command);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // agregamos un producto al item
    @PostMapping("crear-item")
    @Operation(
            summary = "Creamos un item de compra para un usuario",
            description = "Crea un item de los productos que vaya ingresando el usuario",
            tags = {"items"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dato requerido: 'cantidad, precio (Tiene que ser el mismo del producto), usuario (ID), producto (ID)' (required).",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de los items con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = Item.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error validación (e.g. campos o un campo esta vacío)"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict (e.g. el item ya existe)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<Item> agregarItem(@RequestBody Item item) throws Exception {
        try {
            System.out.println("Item recibido: " + item);

            Command<Item> createItemCommand = itemCommandFactory.createCreateItemCommand(item);

            Item itemValidado = createItemCommand.execute();

            // guardamos los datos
            Item itemNuevo = itemService.agregarItem(itemValidado);

            return ResponseEntity.status(HttpStatus.CREATED).body(itemNuevo);
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.out.println("Error al crear un item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("update-item/{id}")
    @Operation(
            summary = "Actualizamos un item por el ID",
            description = "Actualizamos un dato del item de un usuario requiere permisos de USER",
            tags = {"items"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "El id es requerido para buscar uno de los item que tenga un usuario",
                            example = "1",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Actualizamos un dato de los items disponibles",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Actualizada correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Item.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en el formato del dato"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No existe el Item"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<Item> updateItem(@PathVariable("id") Long id, @RequestBody Item item) throws Exception {
        try {
            Command<Item> updateItemCommand = itemCommandFactory.updateItemCommand(id, item);
            Item update = updateItemCommand.execute();
            return ResponseEntity.ok(update);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
