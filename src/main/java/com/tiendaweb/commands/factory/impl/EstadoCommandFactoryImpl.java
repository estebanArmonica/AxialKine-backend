package com.tiendaweb.commands.factory.impl;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.factory.EstadoCommandFactory;
import com.tiendaweb.commands.impl.estado.ListEstadoCommandImpl;
import com.tiendaweb.models.Estado;
import com.tiendaweb.repositories.IEstadoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstadoCommandFactoryImpl implements EstadoCommandFactory {
    private final IEstadoRepository iEstadoRepository;

    public EstadoCommandFactoryImpl(IEstadoRepository iEstadoRepository) {
        this.iEstadoRepository = iEstadoRepository;
    }

    @Override
    public Command<List<Estado>> obtenerTodoEstadoCommand() {
        return new ListEstadoCommandImpl(iEstadoRepository);
    }
}
