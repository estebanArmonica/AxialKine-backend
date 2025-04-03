package com.tiendaweb.services.utils;

import com.tiendaweb.models.Item;
import com.tiendaweb.repositories.IItemRepository;
import com.tiendaweb.services.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ItemServiceImpl implements IItemService {

    @Autowired
    private IItemRepository itemRepo;

    @Override
    public Item agregarItem(Item item) {
        return itemRepo.save(item);
    }

    @Override
    public Item updateItem(Long id, Item item) {
        Item items = itemRepo.findById(id).orElseThrow(() -> new RuntimeException("El item no existe"));

        items.setCantidad(item.getCantidad());
        items.setPrecio(item.getPrecio());
        items.setProducto(item.getProducto());

        return itemRepo.save(item);
    }

    @Override
    public Set<Item> obtenerTodaLista() {
        return new LinkedHashSet<>(itemRepo.findAll());
    }

    @Override
    public Optional<Item> buscarPorIdItem(Long id) {
        return itemRepo.findById(id);
    }
}
