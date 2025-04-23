package com.tiendaweb.commands.factory.impl;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.factory.TareaCommandFactory;
import com.tiendaweb.commands.impl.tarea.CreateTareaCommandImpl;
import com.tiendaweb.commands.impl.tarea.ListIdTareaCommandImpl;
import com.tiendaweb.commands.impl.tarea.ListTareaCommandImpl;
import com.tiendaweb.commands.impl.tarea.UpdateTareaCommandImpl;
import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.repositories.ITareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Component
public class TareaCommandFactoryImpl implements TareaCommandFactory {
    private final IProductoRepository iProductoRepository;
    private final BlobCreator blobCreator;
    private final ITareaRepository iTareaRepository;

    @Autowired
    public TareaCommandFactoryImpl(IProductoRepository iProductoRepository, BlobCreator blobCreator, ITareaRepository iTareaRepository) {
        this.iProductoRepository = iProductoRepository;
        this.blobCreator = blobCreator;
        this.iTareaRepository = iTareaRepository;
    }

    // creamos una tarea
    @Override
    public Command<Tarea> createTareaCommand(Tarea tarea, MultipartFile video) {
        return new CreateTareaCommandImpl(tarea, video, iProductoRepository, blobCreator);
    }

    // actualizamos una tarea
    @Override
    public Command<Tarea> updateTareaCommand(Long id, Tarea tarea, MultipartFile video) {
        return new UpdateTareaCommandImpl(id, tarea, video, iTareaRepository, iProductoRepository, blobCreator);
    }

    // listamos todo
    @Override
    public Command<List<Tarea>> obtenerTareaCommand() {
        return new ListTareaCommandImpl(iTareaRepository);
    }

    // buscamos una tarea pero por id
    @Override
    public Command<Optional<Tarea>> buscarTareaCommand(Long id) {
        return new ListIdTareaCommandImpl(id, iTareaRepository);
    }
}
