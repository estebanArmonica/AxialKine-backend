package com.tiendaweb.commands.factory;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Tarea;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface TareaCommandFactory {

    // creamos una tarea
    Command<Tarea> createTareaCommand(Tarea tarea, MultipartFile videFile);

    // actualizamos una tarea
    Command<Tarea> updateTareaCommand(Long id, Tarea tarea, MultipartFile videoFile);

    // listamos todas las tareas disponibles
    Command<List<Tarea>> obtenerTareaCommand();

    // listamos por el id de una tarea
    Command<Optional<Tarea>> buscarTareaCommand(Long id);
}
