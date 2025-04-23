package com.tiendaweb.adapter.transbank.service;

import com.tiendaweb.models.DetalleCompra;
import com.tiendaweb.models.Item;
import com.tiendaweb.models.transbank.*;
import com.tiendaweb.repositories.ICompraRepository;
import com.tiendaweb.repositories.IDetalleCompraRepository;
import com.tiendaweb.repositories.IItemRepository;
import com.tiendaweb.services.IItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class CompraService {
    private final ICompraRepository iCompraRepository;
    private final PaymentService paymentService;
    private final IItemService itemService;
    private final IDetalleCompraRepository iDetalleCompraRepository;
    private final IItemRepository iItemRepository;

    public CompraService(ICompraRepository iCompraRepository, PaymentService paymentService, IItemService itemService, IDetalleCompraRepository iDetalleCompraRepository, IItemRepository iItemRepository) {
        this.iCompraRepository = iCompraRepository;
        this.paymentService = paymentService;
        this.itemService = itemService;
        this.iDetalleCompraRepository = iDetalleCompraRepository;
        this.iItemRepository = iItemRepository;
    }

    // iniciamos el pago
    @Transactional
    public TransbankTransactionResponse iniciarPago(Item item, String returnUrl) {
        System.out.println("Valor de returnUrl recibido: " + returnUrl);
        String buyOrder = generateBuyOrder();
        System.out.println("Longitud de buyOrder: " + buyOrder.length() + " - Valor: " + buyOrder);


        // validamos el parametro de entrada
        Objects.requireNonNull(returnUrl, "La URL de retorno no puede ser nula");
        if(returnUrl.trim().isEmpty()){
            throw new IllegalArgumentException("La URL de retorno no puede estar vacía");
        }

        // creamos la compra en estado inicial
        Compra compra = new Compra();
        compra.setItem(item);
        compra.setAmount(item.getPrecio() * item.getCantidad());
        compra.setBuyOrder(generateBuyOrder());
        compra.setSessionId(generateSessionId());
        compra.setReturnUrl(returnUrl.trim());
        compra.setStatus("INITIALIZED");

        // guardamos la compra antes de llamar a transbank
        compra = iCompraRepository.save(compra);

        // Iniciar transaccion en Transbank
        TransbankTransactionResponse response = paymentService.initiatePayment(
                compra.getBuyOrder(),
                compra.getSessionId(),
                compra.getAmount(),
                compra.getReturnUrl()
        );

        // guardamos el token en la compra
        compra.setToken(response.getToken());
        iCompraRepository.save(compra);

        // devolvemos la respuesta modificada con la URL correcta
        response.setUrl(response.getUrl() + "?token_ws=" + response.getToken());
        return response;
    }

    // confirmamos el pago
    @Transactional
    public TransbankConfirmation confirmPago(String token) {
        // busca la compra relacionada con el token
        Compra compra = iCompraRepository.findByToken(token);

        // verificamos que el token exista
        if(compra == null){
            throw new RuntimeException("Compra no encontrada para el token: " + token);
        }

        // confirmar con Transbank
        TransbankConfirmation confirmation = paymentService.confirmPaymet(token);

        // actualizamos la compra despues de confirmarla
        compra.setStatus(confirmation.getStatus());
        iCompraRepository.save(compra);

        // creamos y guardamos el detalle de la compra automaticamente
        DetalleCompra detalle = new DetalleCompra();
        detalle.setFechaDetCompra(LocalDate.now());
        detalle.setCompra(compra);
        // guardamos los datos
        iDetalleCompraRepository.save(detalle);

        // limpiamos el item (eliminarlo o marcarlo como vendido)
        Item item = compra.getItem();

        System.out.println("Item antes de limpiar: "+ item);
        if(item != null){
            itemService.limpiarItem(item.getId());
            System.out.println("Item despues de limpiar: " + iItemRepository.findById(item.getId()));
        }
        return confirmation;
    }

    // anulamos el pago
    @Transactional
    public TransbankRefundResponse anularPago(String token, double amount) {
        // anulammos el pago
        TransbankRefundResponse refundResponse = paymentService.refundPayment(token, amount);

        // actualizamso la compra
        Compra compra = iCompraRepository.findByToken(token);

        compra.setStatus("ANULADA");
        iCompraRepository.save(compra);

        return refundResponse;
    }

    // obtenemos el estado de un pago si esque aún no hemos confirmado la transacción
    @Transactional
    public TransbankTransactionStatus obtenerEstadoTransaccion(String token) {
        // validamos token
        if(token == null || token.isBlank()){
            throw new IllegalArgumentException("Token no puede estar vacío");
        }

        // obtenemos el estado desde Transbank
        TransbankTransactionStatus estado = paymentService.checkTransactionStatus(token);

        /* Opcional: Actualizamos el estado
        Optional<Compra> compraOpt = compraRepository.findByToken(token);
        compraOpt.ifPresent(compra -> {
            compra.setStatus(estado.getStatus());
            compraRepository.save(compra);
        });*/
        return estado;
    }

    // generamos el orden de compra (la serie de buy_order)
    private String generateBuyOrder() {

        // formato más corto: "B0-+timestap + 4 digtos aleatorios
        String timestap = String.valueOf(System.currentTimeMillis() / 1000);

        // segundos en lugar de milisegundos
        String random = String.format("%04d", new Random().nextInt(10000));

        // retornamos un maxímo de 20
        return "BO-" + timestap.substring(timestap.length() - 8) + "-" + random;
    }

    private String generateSessionId() {
        return "SESS-" + UUID.randomUUID().toString() + "-" + LocalDateTime.now().hashCode();
    }
}
