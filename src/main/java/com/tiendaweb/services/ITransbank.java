package com.tiendaweb.services;

import com.tiendaweb.models.transbank.Compra;

public interface ITransbank {
    // creamos una transaccion
    String createTransaction(Compra compra);

    // confirmamos la transaccion
    String confirmTransaction(String token);

    // reservamos el producto
    String reservePaymentOrCancel(String token, int amount);

    // capturamos un transaccion
    String captureTransaction(String token, String buyOrder, String authorizationCode, int captureAmount);

    // obtenemos el estado de una compra
    String transactionStatus(String token);
}
