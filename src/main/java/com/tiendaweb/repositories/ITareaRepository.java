package com.tiendaweb.repositories;

import com.tiendaweb.models.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITareaRepository extends JpaRepository<Tarea, Long> {
    Optional<Tarea> findById(Long id);
}
