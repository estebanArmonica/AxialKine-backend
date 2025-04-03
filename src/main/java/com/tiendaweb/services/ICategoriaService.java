package com.tiendaweb.services;

import com.tiendaweb.models.Categoria;

import java.util.Set;

public interface ICategoriaService {
    // agregamos una categoria
    Categoria agregarCategoria(Categoria cate);

    // actualizamos una categoria
    Categoria updateCategoria(Long idCate, String descripcion);

    // Listamos todas las categorias
    Set<Categoria> obtenerTodo();

    // obtenemos la cateogria por el ID
    Categoria obtenerIdCategoria(Long idCate);
}
