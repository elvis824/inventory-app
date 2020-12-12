package com.elvison.inventoryapp.repository;

import com.elvison.inventoryapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    List<Product> findByNameLike(String nameFilter);
}
