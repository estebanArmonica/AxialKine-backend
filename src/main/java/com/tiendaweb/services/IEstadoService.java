package com.tiendaweb.services;

import com.tiendaweb.models.Estado;

import java.util.Set;

public interface IEstadoService {
    // agregamos una categoria
    Estado agregarEstado(Estado estado);

    // actualizamos una categoria
    Estado updateEstado(Long idEstado, String tipo);

    // Listamos todas las categorias
    Set<Estado> obtenerTodo();

    // obtenemos la cateogria por el ID
    Estado obtenerIdEstado(Long idEstado);
}
