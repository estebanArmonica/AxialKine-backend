package com.tiendaweb.services;

import com.tiendaweb.models.Producto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface IProductoService {
    // agregamos un producto
    Producto agregarProducto(Producto prod);

    // actualizamos un producto
    Producto updateProducto(int codigo, Producto prod);

    // listamos toda la lista
    Set<Producto> obtenerTodo();

    // buscamos por el codigo del producto
    Optional<Producto> buscarPorCodigo(int codigo);

    // creamos el export a excel
    void exportExcel(final HttpServletResponse response) throws IOException, Exception;
}
