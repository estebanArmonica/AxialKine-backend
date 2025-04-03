package com.tiendaweb.repositories;

import com.tiendaweb.models.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByTipo(String tipo);
}
