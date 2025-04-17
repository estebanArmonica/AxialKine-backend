package com.tiendaweb.commands.impl.categoria;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Categoria;

public class UpdateCategoryCommandImpl implements Command<Categoria> {
    private final Long id;
    private final Categoria cate;

    public UpdateCategoryCommandImpl(Long id, Categoria cate) {
        this.id = id;
        this.cate = cate;
    }

    // getter
    public Long getId(){
        return id;
    }

    public Categoria getCate() {
        return cate;
    }

    @Override
    public Categoria execute() {
        throw new UnsupportedOperationException("Este comando debe ser procesado por un CommandHandler");
    }
}
