package com.tiendaweb.controllers;

import com.tiendaweb.adapter.transbank.service.CompraService;
import com.tiendaweb.exception.BusinessException;
import com.tiendaweb.exception.ResourceNotFoundException;
import com.tiendaweb.exception.TransbankException;
import com.tiendaweb.models.Item;
import com.tiendaweb.models.transbank.*;
import com.tiendaweb.repositories.ICompraRepository;
import com.tiendaweb.services.IItemService;
import com.tiendaweb.strategys.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.TransactionalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/payments/")
@Tag(name = "Forma de pago", description = "Controlador de Pagos Transbank")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET})
public class PaymentController {
    private final CompraService compraService;
    private final ICompraRepository compraRepo;
    private final IItemService itemService;
    private final BoletaService boletaService;

    @Autowired
    public PaymentController(CompraService compraService, ICompraRepository compraRepo, IItemService itemService, BoletaService boletaService) {
        this.compraService = compraService;
        this.compraRepo = compraRepo;
        this.itemService = itemService;
        this.boletaService = boletaService;
    }

    // inicializa una compra
    @PostMapping("iniciar-pago")
    @Operation(summary = "Iniciar Pago", description = "Crea una transaccion en Transbank (Ambiente de pruebas)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago iniciado",
                    content = @Content(schema = @Schema(implementation = TransbankTransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Item no encontrado"),
            @ApiResponse(responseCode = "502", description = "Error en comunicación con Transbank")
    })
    public ResponseEntity<?> iniciarPago(@RequestParam Long itemId, @RequestParam String returnUrl) {
        try {
            // validamos la URL explicita
            if(returnUrl == null || returnUrl.trim().isEmpty()){
                throw new IllegalArgumentException("El parametro returnUrl es obligatoria");
            }

            Item item = itemService.buscarPorIdItem(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item no encontrado"));

            TransbankTransactionResponse response = compraService.iniciarPago(item, returnUrl);
            return ResponseEntity.ok(response);
        } catch(IllegalArgumentException e){
            System.err.println("BAD REQUEST: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch(Exception e) {
            System.err.println("INTERNAL SERVER ERROR: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error el iniciar el pago: " + e.getMessage());
        }
    }

    // generamos el pdf de boleta
    @GetMapping("generar-pdf")
    @Operation(
            summary = "Generacion de archivos de boletas electronicas",
            description = "Genera una boleta electronica de una transaccion confirmada",
            tags = {"Forma de pago"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Creacion de una boleta electronica, exitosa",
                            content = @Content(mediaType = "application/json",
                                               schema = @Schema(type = "array", implementation = Compra.class))
                    )
            }
    )
    public void generarBoletaPorToken(HttpServletResponse response, @RequestParam String token) throws IOException {
        try {
          boletaService.generarBoletaElectronica(response, token);
        } catch(ResourceNotFoundException e) {
            System.out.println("RESOURCE NOT FOUND EXCEPTION: " + e.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().write("Compra no encontrada");
            response.getWriter().flush();
        } catch(BusinessException e) {
            System.out.println("BUSINESS EXCEPTION: " + e.getMessage());
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            response.getWriter().write("No se puede generar la boleta: " + e.getMessage());
            response.getWriter().flush();
        } catch(Exception e) {
            System.out.println("INTERNAL SERVER ERROR: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error interno del servidor");
            response.getWriter().flush();
        }
    }

    // verifica el estado de una compra
    @GetMapping("estado-transaccion")
    @Operation(summary = "Obtener estado de transacción",
            description = "Consulta el estado actual de una transacción en Transbank")
    public ResponseEntity<?> obtenerEstadoTransaccion(@RequestParam String token){
        try {
            TransbankTransactionStatus estado = compraService.obtenerEstadoTransaccion(token);
            return ResponseEntity.ok(estado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (TransbankException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Error al consultar Transbank: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al consultar estado");
        }
    }

    // confirma una compra
    @PutMapping("confirmar-pago")
    @Operation(summary = "Confirma una transaccion para su compra",
            description = "Confirma el proceso de la realizacion del pago para realizar compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago confirmado y boleta generada",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TransbankConfirmation.class))
                }
            ),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
            @ApiResponse(responseCode = "502", description = "Error en comunicación con Transbank")
    })
    public ResponseEntity<?> confirmarPago(@RequestParam String token) {
        try {
            // buscamos la compra usando el token
            Compra compra = compraRepo.findByToken(token);

            // confirmamos el pago con Transbank
            TransbankConfirmation confirmation = compraService.confirmPago(token);

            // actualizamso el estado de la compra
            compra.setStatus(confirmation.getStatus());
            compraRepo.save(compra);

            // confirmamos la compra y devolvemos el id de la compra
            Map<String, Object> response = new HashMap<>();
            response.put("compraId", compra.getId());  // Devuelve el ID de la compra
            response.put("transbankConfirmation", confirmation);
            response.put("boletaUrl", "/api/boletas/" + compra.getId());  // URL directa a la boleta
            response.put("message", "Pago confirmado exitosamente");

            return ResponseEntity.ok(confirmation);
        } catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(TransactionalException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error al comunicarse con Transbank: " + e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al confirmar pago: " + e.getMessage());
        }
    }

    // anula o reserva compras
    @PostMapping("anular-pago")
    @Operation(summary = "Anulamos o Reservamos una transaccion",
            description = "Anual o reserva una compra (RESERVED o NULLIFIED)")
    public ResponseEntity<?> anularCompra(@RequestParam String token, @RequestParam double amount){
        try {
            //obtenemos el estado
            TransbankTransactionStatus status = compraService.obtenerEstadoTransaccion(token);

            // validamos si es anulable
            if(!List.of("AUTHORIZED", "CAPTURED").contains(status.getStatus())){
                return ResponseEntity.unprocessableEntity()
                        .body(Map.of(
                                "error", "Transacción no anulable",
                                "current_status", status.getStatus()
                        ));
            }

            // procede con anulacion
            TransbankRefundResponse response = compraService.anularPago(token, amount);
            return ResponseEntity.ok(response);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al anular pago: " + e.getMessage());
        }
    }
}
