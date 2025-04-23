package com.tiendaweb.commands.factory;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Estado;

import java.util.List;

public interface EstadoCommandFactory {

    Command<List<Estado>> obtenerTodoEstadoCommand();
}
