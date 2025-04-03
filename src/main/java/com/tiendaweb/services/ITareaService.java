package com.tiendaweb.services;

import com.tiendaweb.models.Tarea;

import java.util.Optional;
import java.util.Set;

public interface ITareaService {

    // agregamos una tarea a un producto
    Tarea agregarTarea(Tarea tarea);

    // actualizar una tarea
    Tarea updateTarea(Long id, Tarea tarea);

    // listamos toda la lista
    Set<Tarea> obtenerTodoTarea();

    // buscamos por el id
    Optional<Tarea> buscarPorIdTarea(Long id);
}
