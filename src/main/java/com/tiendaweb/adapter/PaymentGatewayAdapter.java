package com.tiendaweb.adapter;

import com.tiendaweb.models.transbank.*;

public interface PaymentGatewayAdapter {
    // creamos una transaccion con compra
    TransbankTransactionResponse createTransaction(Compra compra);

    // este solo mantiene la comunicacion con transbank
    TransbankTransactionResponse createTransaction(TransbankTransactionRequest request);


    // confirmamos una transaccion
    TransbankConfirmation confirmTransaction(String token);

    TransbankRefundResponse refundTransaction(String token, double amount);

    TransbankTransactionStatus checkTransactionStatus(String token);
}
