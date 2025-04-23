package com.tiendaweb.commands.factory;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductoCommandFactory {

    // creamos un producto (POST)
    Command<Producto> createProductoCommand(Producto producto, MultipartFile imagenFile);

    // actualizamos un producto, buscando su codigo (PUT)
    Command<Producto> updateProductoCommand(int codigo, Producto producto, MultipartFile imagenFile);

    // listamos todos los productos disponibles
    Command<List<Producto>> obtenerTodoProductoCommand();

    // listamos por el id (codigo) del un producto
    Command<Optional<Producto>> buscarProductoCommand(int codigo);
}
