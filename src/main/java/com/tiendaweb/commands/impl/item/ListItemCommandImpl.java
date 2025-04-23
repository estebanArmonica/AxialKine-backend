package com.tiendaweb.commands.impl.item;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Item;
import com.tiendaweb.repositories.IItemRepository;

import java.util.List;

public class ListItemCommandImpl implements Command<List<Item>> {
    private final IItemRepository iItemRepository;

    public ListItemCommandImpl(IItemRepository iItemRepository) {
        this.iItemRepository = iItemRepository;
    }

    @Override
    public List<Item> execute() {
        return iItemRepository.findAll();
    }
}
