package com.tiendaweb.commands.factory.impl;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.impl.item.CreateItemCommandImpl;
import com.tiendaweb.commands.factory.ItemCommandFactory;
import com.tiendaweb.commands.impl.item.ListIdItemCommandImpl;
import com.tiendaweb.commands.impl.item.ListItemCommandImpl;
import com.tiendaweb.commands.impl.item.UpdateItemCommandImpl;
import com.tiendaweb.models.Item;
import com.tiendaweb.repositories.IItemRepository;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ItemCommandFactoryImpl implements ItemCommandFactory {
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IItemRepository iItemRepository;

    @Autowired
    public ItemCommandFactoryImpl(IProductoRepository productoRepository, IUsuarioRepository usuarioRepository, IItemRepository iItemRepository) {
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.iItemRepository = iItemRepository;
    }

    @Override
    public Command<Item> createCreateItemCommand(Item item) {
        return new CreateItemCommandImpl(item, productoRepository, usuarioRepository);
    }

    @Override
    public Command<Item> updateItemCommand(Long id, Item item) {
        return new UpdateItemCommandImpl(id, item, iItemRepository);
    }

    @Override
    public Command<List<Item>> createListItemsCommand() {
        return new ListItemCommandImpl(iItemRepository);
    }

    @Override
    public Command<Optional<Item>> createListItemByIdCommand(Long itemId) {
        return new ListIdItemCommandImpl(itemId, iItemRepository);
    }
}
