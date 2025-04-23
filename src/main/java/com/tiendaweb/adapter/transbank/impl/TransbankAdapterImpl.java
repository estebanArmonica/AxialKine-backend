package com.tiendaweb.adapter.transbank.impl;

import com.tiendaweb.adapter.PaymentGatewayAdapter;
import com.tiendaweb.exception.TransbankException;
import com.tiendaweb.models.transbank.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class TransbankAdapterImpl implements PaymentGatewayAdapter {
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String commerceCode;
    private final String baseUrl;

    public TransbankAdapterImpl(RestTemplate restTemplate,
                            @Value("${transbank.api.key}") String apiKey,
                            @Value("${transbank.commerce.code}") String commerceCode,
                            @Value("${transbank.api.baseurl}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.commerceCode = commerceCode;
        this.baseUrl = baseUrl;
    }

    public TransbankTransactionResponse createTransaction(Compra compra) {
        TransbankTransactionRequest request = new TransbankTransactionRequest(
                compra.getBuyOrder(),
                compra.getSessionId(),
                compra.getAmount(),
                compra.getReturnUrl()
        );
        return createTransaction(request);
    }

    @Override
    public TransbankTransactionResponse createTransaction(TransbankTransactionRequest request) {
        String url = baseUrl + "/rswebpaytransaction/api/webpay/v1.2/transactions";

        HttpHeaders headers = createHeaders();
        HttpEntity<TransbankTransactionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TransbankTransactionResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                TransbankTransactionResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new TransbankException("Error al crear transacción en Transbank. CODIGO: " + response.getStatusCode());
    }

    @Override
    public TransbankConfirmation confirmTransaction(String token) {
        String url = baseUrl + "/rswebpaytransaction/api/webpay/v1.2/transactions/" + token;

        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<TransbankConfirmation> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                TransbankConfirmation.class
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new RuntimeException("Error al confirmar transacción en Transbank");
    }

    // anulamos una compra
    @Override
    public TransbankRefundResponse refundTransaction(String token, double amount) {
        // verificamos el estado
        TransbankTransactionStatus status = checkTransactionStatus(token);

        String url = baseUrl + "/rswebpaytransaction/api/webpay/v1.2/transactions/"+token+"/refunds";

        Map<String, Double> requestBoyd = new HashMap<>();
        requestBoyd.put("amount", amount);

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Double>> entity = new HttpEntity<>(requestBoyd, headers);

        ResponseEntity<TransbankRefundResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                TransbankRefundResponse.class
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new RuntimeException("Error al anular transacción en Transbank");
    }

    // consulta por el status de un producto antes de realizar el pago
    @Override
    public TransbankTransactionStatus checkTransactionStatus(String token) {
        String url = baseUrl + "/rswebpaytransaction/api/webpay/v1.2/transactions/"+token;

        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<TransbankTransactionStatus> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TransbankTransactionStatus.class
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new TransbankException("Error al obtener estado de transacción");
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tbk-Api-Key-Id", commerceCode);
        headers.set("Tbk-Api-Key-Secret", apiKey);
        return headers;
    }
}
