package com.tiendaweb.commands.impl.categoria;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Categoria;

public class CreateCategoryCommandImpl implements Command<Categoria> {
    private final Categoria cate;

    public CreateCategoryCommandImpl(Categoria cate) {
        this.cate = cate;
    }

    @Override
    public Categoria execute() {
        return cate;
    }
}
