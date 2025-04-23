package com.tiendaweb.commands.handlers;

import com.tiendaweb.commands.impl.categoria.CreateCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.ListCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.ListIdCategoryCommandImpl;
import com.tiendaweb.commands.impl.categoria.UpdateCategoryCommandImpl;
import com.tiendaweb.models.Categoria;
import com.tiendaweb.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CategoryCommandHandler {
    private final ICategoriaService cateService;

    @Autowired
    public CategoryCommandHandler(ICategoriaService cateService) {
        this.cateService = cateService;
    }

    // listamos todas las categorias (GET)
    @Transactional(readOnly = true)
    public Set<Categoria> handle(ListCategoryCommandImpl command) {
        return cateService.obtenerTodo();
    }

    // listamos con el id de una categoria (GET/ID)
    @Transactional(readOnly = true)
    public Categoria handle(ListIdCategoryCommandImpl command) {
        return cateService.obtenerIdCategoria(command.getId());
    }

    // creamos una categoria (POST)
    public Categoria handle(CreateCategoryCommandImpl command) {
        // validamos los datos
        if(command.execute().getDescripcion() == null){
            throw new IllegalArgumentException("La descripcion es requerida");
        }
        return cateService.agregarCategoria(command.execute());
    }

    // actualizamos una categoria (PUT)
    public Categoria handle(UpdateCategoryCommandImpl command) {
        try {
            return cateService.updateCategoria(command.getId(), command.getCate().getDescripcion());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos: ", e);
        }
    }
}
