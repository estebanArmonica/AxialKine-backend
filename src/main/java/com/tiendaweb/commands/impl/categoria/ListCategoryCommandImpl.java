package com.tiendaweb.commands.impl.categoria;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Categoria;

import java.util.Set;

public class ListCategoryCommandImpl implements Command<Set<Categoria>> {

    public ListCategoryCommandImpl(){}

    @Override
    public Set<Categoria> execute() {
        throw new UnsupportedOperationException("Este comando debe ser procesado por un CommandHandler");
    }
}
