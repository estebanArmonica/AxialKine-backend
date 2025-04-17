package com.tiendaweb.commands.impl.estado;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Estado;

import java.util.Set;

public class ListEstadoCommandImpl implements Command<Set<Estado>> {

    public ListEstadoCommandImpl(){}

    @Override
    public Set<Estado> execute() {
        throw new UnsupportedOperationException("Este comando de ser procesado por un CommandHandler");
    }
}
