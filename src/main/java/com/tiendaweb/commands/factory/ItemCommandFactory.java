package com.tiendaweb.commands.factory;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Item;

import java.util.Optional;
import java.util.List;

public interface ItemCommandFactory {

    // creamos un item
    Command<Item> createCreateItemCommand(Item item);

    // actualizar un item
    Command<Item> updateItemCommand(Long id, Item item);

    // listamos por el id de un usuario
    Command<List<Item>> createListItemsCommand();

    // listamos por id del item
    Command<Optional<Item>> createListItemByIdCommand(Long itemId);
}
