package com.tiendaweb.repositories;

import com.tiendaweb.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {
    // buscamos un rol por su nombre
    Optional<Rol> findByTipoRol(String tipoRol);
}
