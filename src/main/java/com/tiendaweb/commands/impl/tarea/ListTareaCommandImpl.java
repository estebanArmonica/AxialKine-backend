package com.tiendaweb.commands.impl.tarea;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.ITareaRepository;

import java.util.List;

public class ListTareaCommandImpl implements Command<List<Tarea>> {
    private final ITareaRepository iTareaRepository;

    public ListTareaCommandImpl(ITareaRepository iTareaRepository) {
        this.iTareaRepository = iTareaRepository;
    }

    @Override
    public List<Tarea> execute() {
        return iTareaRepository.findAll();
    }
}
