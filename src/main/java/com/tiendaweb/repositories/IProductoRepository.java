package com.tiendaweb.repositories;

import com.tiendaweb.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsByCodigo(int codigo);

    boolean existsByNombre(String nombre);

    // buscamos el codigo
    Optional<Producto> findByCodigo(int codigo);
}
