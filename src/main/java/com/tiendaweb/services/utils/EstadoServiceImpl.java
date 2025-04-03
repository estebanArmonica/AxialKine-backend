package com.tiendaweb.services.utils;

import com.tiendaweb.models.Estado;
import com.tiendaweb.repositories.IEstadoRepository;
import com.tiendaweb.services.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class EstadoServiceImpl implements IEstadoService {

    @Autowired
    private IEstadoRepository estadoRepo;

    public EstadoServiceImpl(IEstadoRepository estadoRepo){
        this.estadoRepo = estadoRepo;
    }

    @Override
    public Estado agregarEstado(Estado estado) {
        return estadoRepo.save(estado);
    }

    @Override
    public Estado updateEstado(Long idEstado, String tipo) {
        Estado estado = estadoRepo.findById(idEstado).orElseThrow(() -> new RuntimeException("Estado no encontrada"));
        estado.setTipo(tipo);
        return estadoRepo.save(estado);
    }

    @Override
    public Set<Estado> obtenerTodo() {
        return new LinkedHashSet<>(estadoRepo.findAll());
    }

    @Override
    public Estado obtenerIdEstado(Long idEstado) {
        return estadoRepo.findById(idEstado).get();
    }
}
