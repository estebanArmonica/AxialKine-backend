package com.tiendaweb.commands.impl.estado;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Estado;
import com.tiendaweb.repositories.IEstadoRepository;

import java.util.List;
import java.util.Set;

public class ListEstadoCommandImpl implements Command<List<Estado>> {
    private final IEstadoRepository iEstadoRepository;

    public ListEstadoCommandImpl(IEstadoRepository iEstadoRepository) {
        this.iEstadoRepository = iEstadoRepository;
    }

    @Override
    public List<Estado> execute() {
        return iEstadoRepository.findAll();
    }
}
