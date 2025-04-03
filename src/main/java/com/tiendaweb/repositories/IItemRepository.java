package com.tiendaweb.repositories;

import com.tiendaweb.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long id);
}
