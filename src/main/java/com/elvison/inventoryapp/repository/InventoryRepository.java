package com.elvison.inventoryapp.repository;

import com.elvison.inventoryapp.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    boolean existsByName(String name);

    List<Inventory> findByNameLike(String nameFilter);
}
