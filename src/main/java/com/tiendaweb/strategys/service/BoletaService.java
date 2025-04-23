package com.tiendaweb.strategys.service;

import com.tiendaweb.models.transbank.Compra;
import com.tiendaweb.repositories.ICompraRepository;
import com.tiendaweb.strategys.GeneradorBoletaStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class BoletaService {

    private final GeneradorBoletaStrategy generadorBoletaStrategy;
    private final ICompraRepository compraRepository;

    @Autowired
    public BoletaService(GeneradorBoletaStrategy generadorBoletaStrategy, ICompraRepository compraRepository) {
        this.generadorBoletaStrategy = generadorBoletaStrategy;
        this.compraRepository = compraRepository;
    }

    public void generarBoletaElectronica(HttpServletResponse response, String token) throws IOException {
        Compra compra = compraRepository.findByToken(token);

        ByteArrayOutputStream pdfOutput = generadorBoletaStrategy.generar(compra);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=boleta-axialkine.pdf");
        response.getOutputStream().write(pdfOutput.toByteArray());
    }
}
