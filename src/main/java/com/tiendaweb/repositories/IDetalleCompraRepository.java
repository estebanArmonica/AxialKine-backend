package com.tiendaweb.repositories;

import com.tiendaweb.models.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
}
