package com.tiendaweb.services.utils;

import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.ITareaRepository;
import com.tiendaweb.services.ITareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TareaServiceImpl implements ITareaService {

    @Autowired
    private ITareaRepository tareaRepo;

    public TareaServiceImpl(ITareaRepository tareaRepo) {
        this.tareaRepo = tareaRepo;
    }

    @Override
    public Tarea agregarTarea(Tarea tarea) {
        return tareaRepo.save(tarea);
    }

    @Override
    public Tarea updateTarea(Long id, Tarea tarea) {
        Tarea tareas = tareaRepo.findById(id).orElseThrow(() -> new RuntimeException("Tarea del producto no encontrada"));
        tareas.setNombre(tarea.getNombre());
        tareas.setVideo(tarea.getVideo());
        tareas.setProducto(tarea.getProducto());

        return tareaRepo.save(tareas);
    }

    @Override
    public Set<Tarea> obtenerTodoTarea() {
        return new LinkedHashSet<>(tareaRepo.findAll());
    }

    @Override
    public Optional<Tarea> buscarPorIdTarea(Long id) {
        return tareaRepo.findById(id);
    }
}
