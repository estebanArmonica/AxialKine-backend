package com.tiendaweb.commands.impl.categoria;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Categoria;

public class ListIdCategoryCommandImpl implements Command<Categoria> {

    private final Long id;

    public ListIdCategoryCommandImpl(Long id) {
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    @Override
    public Categoria execute() {
        throw new UnsupportedOperationException("Este comando debe ser procesado por un CommandHandler");
    }
}
