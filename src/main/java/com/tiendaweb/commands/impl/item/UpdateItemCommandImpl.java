package com.tiendaweb.commands.impl.item;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Item;
import com.tiendaweb.repositories.IItemRepository;

public class UpdateItemCommandImpl implements Command<Item> {
    private final Long id;
    private final Item item;
    private final IItemRepository iItemRepository;

    public UpdateItemCommandImpl(Long id, Item item, IItemRepository iItemRepository) {
        this.id = id;
        this.item = item;
        this.iItemRepository = iItemRepository;
    }

    @Override
    public Item execute() {
        // Validar que el item exista
        Item itemExiste = iItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El item no existe"));

        // Resetear los valores según tu lógica actual
        itemExiste.setCantidad(0);
        itemExiste.setPrecio(0);
        itemExiste.setProducto(null);
        return iItemRepository.save(itemExiste);
    }
}
