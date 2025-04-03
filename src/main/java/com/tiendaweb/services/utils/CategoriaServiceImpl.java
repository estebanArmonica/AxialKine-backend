package com.tiendaweb.services.utils;

import com.tiendaweb.models.Categoria;
import com.tiendaweb.repositories.ICategoriaRepository;
import com.tiendaweb.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private ICategoriaRepository cateRepo;

    public CategoriaServiceImpl(ICategoriaRepository cateRepo){
        this.cateRepo = cateRepo;
    }

    // agregamos una categoria
    @Override
    public Categoria agregarCategoria(Categoria cate) {
        return cateRepo.save(cate);
    }

    // actualizamos una categoria
    @Override
    public Categoria updateCategoria(Long idCate, String descripcion) {
        /*
         * con findById lo que hacemos es buscar la ID de la categoria que vamos a actualizar
         * si el id no existe se nos mostrara un else especificando que el id no existe o no fue encontrado
         * si existe el ID, podremos actualizar la descripcion de la categoria
         */
        Categoria cate = cateRepo.findById(idCate).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        cate.setDescripcion(descripcion);
        return cateRepo.save(cate);
    }

    // listamos toda la lista
    @Override
    public Set<Categoria> obtenerTodo() {
        // con findAll listamos todo
        return new LinkedHashSet<>(cateRepo.findAll());
    }

    // obtenemos una categoria por el id
    @Override
    public Categoria obtenerIdCategoria(Long idCate) {
        return cateRepo.findById(idCate).get();
    }

}
