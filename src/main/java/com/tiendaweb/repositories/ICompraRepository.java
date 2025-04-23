package com.tiendaweb.repositories;

import com.tiendaweb.models.transbank.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompraRepository extends JpaRepository<Compra, Integer> {
    Compra findByBuyOrder(String buyOrder);
    Compra findByToken(String token);
}
