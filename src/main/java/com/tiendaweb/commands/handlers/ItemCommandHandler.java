package com.tiendaweb.commands.handlers;

import com.tiendaweb.commands.impl.item.CreateItemCommandImpl;
import com.tiendaweb.commands.impl.item.ListIdItemCommandImpl;
import com.tiendaweb.commands.impl.item.ListItemCommandImpl;
import com.tiendaweb.models.Item;
import com.tiendaweb.services.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ItemCommandHandler {
    private final IItemService itemService;

    @Autowired
    public ItemCommandHandler(IItemService itemService) {
        this.itemService = itemService;
    }

    // listamos todos los items (GET)
    @Transactional(readOnly = true)
    public Set<Item> handle(ListItemCommandImpl command) {
        return itemService.obtenerTodaLista();
    }

    // listamos un item por el id (GET/ID)
    @Transactional(readOnly = true)
    public Optional<Item> handle(ListIdItemCommandImpl command) {
        return itemService.buscarPorIdItem(command.getId());
    }

    // creamos un item (POST)
    public Item handle(CreateItemCommandImpl command) {
        // validamos los datos
        if(command.execute().getCantidad() <= 0 || command.execute().getPrecio() <= 0){
            throw new IllegalArgumentException("La cantidad y el precio es requerida");
        }
        if(command.execute().getProducto() == null || command.execute().getUser() == null){
            throw new IllegalArgumentException("Producto y User es requerida");
        }
        return itemService.agregarItem(command.execute());
    }
}
