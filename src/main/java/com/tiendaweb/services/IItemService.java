package com.tiendaweb.services;

import com.tiendaweb.models.Item;

import java.util.Optional;
import java.util.Set;

public interface IItemService {

    // agregamos un item al producto
    Item agregarItem(Item item);

    // actualizar un item al producto
    Item updateItem(Long id, Item item);

    // listamo todo
    Set<Item> obtenerTodaLista();

    // buscamos por el id del item para listar solo los producto de un usuario
    Optional<Item> buscarPorIdItem(Long id);
}
