package com.elvison.inventoryapp.repository;

import com.elvison.inventoryapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    List<Category> findByNameLike(String nameFilter);
}
