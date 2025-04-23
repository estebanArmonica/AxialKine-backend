package com.tiendaweb.strategys;

import com.tiendaweb.models.transbank.Compra;

import java.io.ByteArrayOutputStream;

public interface GeneradorBoletaStrategy {
    ByteArrayOutputStream generar(Compra compra);
}
