package com.tiendaweb.commands.impl.tarea;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.ITareaRepository;

import java.util.Optional;

public class ListIdTareaCommandImpl implements Command<Optional<Tarea>> {
    private final Long id;
    private final ITareaRepository iTareaRepository;

    public ListIdTareaCommandImpl(Long id, ITareaRepository iTareaRepository) {
        this.id = id;
        this.iTareaRepository = iTareaRepository;
    }

    public Long getId(){
        return id;
    }

    @Override
    public Optional<Tarea> execute() {
        if(id == null || id <= 0){
            throw new UnsupportedOperationException("ID de tarea inválido");
        }
        return iTareaRepository.findById(id);
    }
}
