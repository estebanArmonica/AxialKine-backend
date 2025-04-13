package com.tiendaweb.repositories;

import com.tiendaweb.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    // verificamos si un usuario existe en nuestra base de datos
    boolean existsByUsername(String username);

    // buscamos el rol del usuario cuyo sesa igual a 2
    List<Usuario> findByRolId(long idRol);

    // obtenemos todos los usuarios
    List<Usuario> findAll();
}
