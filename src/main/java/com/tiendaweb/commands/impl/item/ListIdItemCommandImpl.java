package com.tiendaweb.commands.impl.item;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Item;
import com.tiendaweb.repositories.IItemRepository;

import java.util.Optional;

public class ListIdItemCommandImpl implements Command<Optional<Item>> {
    private final Long id;
    private final IItemRepository iItemRepository;

    public ListIdItemCommandImpl(Long id, IItemRepository iItemRepository) {
        this.id = id;
        this.iItemRepository = iItemRepository;
    }

    public Long getId(){
        return id;
    }

    @Override
    public Optional<Item> execute() {
        if(id == null || id <= 0){
            throw new UnsupportedOperationException("ID de item inválido");
        }
        return iItemRepository.findById(id);
    }
}
