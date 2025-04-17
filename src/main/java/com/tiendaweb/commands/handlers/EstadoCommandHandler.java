package com.tiendaweb.commands.handlers;

import com.tiendaweb.commands.impl.estado.ListEstadoCommandImpl;
import com.tiendaweb.models.Estado;
import com.tiendaweb.services.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class EstadoCommandHandler {
    private final IEstadoService estadoService;

    @Autowired
    public EstadoCommandHandler(IEstadoService estadoService) {
        this.estadoService = estadoService;
    }

    // listamos todos los estados de un producto (GET)
    public Set<Estado> handle(ListEstadoCommandImpl command) {
        return estadoService.obtenerTodo();
    }
}
