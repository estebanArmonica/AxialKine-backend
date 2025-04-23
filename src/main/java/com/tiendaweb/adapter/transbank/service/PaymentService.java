package com.tiendaweb.adapter.transbank.service;

import com.tiendaweb.adapter.PaymentGatewayAdapter;
import com.tiendaweb.adapter.transbank.impl.TransbankAdapterImpl;
import com.tiendaweb.models.transbank.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentGatewayAdapter paymentGatewayAdapter;

    public PaymentService(PaymentGatewayAdapter paymentGatewayAdapter) {
        this.paymentGatewayAdapter = paymentGatewayAdapter;
    }

    // iniciamos el pago
    public TransbankTransactionResponse initiatePayment(Compra compra) {
        if (paymentGatewayAdapter instanceof TransbankAdapterImpl) {
            return ((TransbankAdapterImpl) paymentGatewayAdapter).createTransaction(compra);
        }
        throw new UnsupportedOperationException("Operación no soportada por el adaptador actual");
    }

    public TransbankTransactionResponse initiatePayment(String buyOrder, String sessionId, double amount, String returnUrl) {
        TransbankTransactionRequest request = new TransbankTransactionRequest(
                buyOrder,
                sessionId,
                amount,
                returnUrl
        );
        return paymentGatewayAdapter.createTransaction(request);
    }

    // confirmamos una transaccion
    public TransbankConfirmation confirmPaymet(String token) {
        return paymentGatewayAdapter.confirmTransaction(token);
    }

    // consultamos el status
    public TransbankTransactionStatus checkTransactionStatus(String token) {
        return paymentGatewayAdapter.checkTransactionStatus(token);
    }

    // anulamos un pago
    public TransbankRefundResponse refundPayment(String token, double amount) {
        return paymentGatewayAdapter.refundTransaction(token, amount);
    }
}
