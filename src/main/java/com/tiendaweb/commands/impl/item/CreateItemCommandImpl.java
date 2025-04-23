package com.tiendaweb.commands.impl.item;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Item;
import com.tiendaweb.models.Producto;
import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.repositories.IUsuarioRepository;

public class CreateItemCommandImpl implements Command<Item> {
    private final Item item;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;

    public CreateItemCommandImpl(Item item, IProductoRepository productoRepository, IUsuarioRepository usuarioRepository) {
        this.item = item;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Item execute() {
        // Validar y obtener producto
        if (item.getProducto() == null || item.getProducto().getCodigo() == 0) {
            throw new IllegalArgumentException("El producto debe ser proporcionado y su código debe ser un valor positivo");
        }

        Producto productoExiste = productoRepository.findById(item.getProducto().getCodigo())
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe o no fue proporcionado"));
        item.setProducto(productoExiste);

        // Validar y obtener usuario
        if (item.getUser() == null || item.getUser().getId() == null) {
            throw new IllegalArgumentException("El usuario debe estar proporcionado y tener un ID válido");
        }

        Usuario usuarioExiste = usuarioRepository.findById(item.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe o no esta creado"));
        item.setUser(usuarioExiste);

        // Validar cantidad y precio
        if (item.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        if (item.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        return item;
    }
}
