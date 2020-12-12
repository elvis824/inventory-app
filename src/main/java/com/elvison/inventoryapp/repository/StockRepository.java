package com.elvison.inventoryapp.repository;

import com.elvison.inventoryapp.model.StockEntry;
import com.elvison.inventoryapp.model.StockEntryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<StockEntry, StockEntryId> {
    List<StockEntry> findByIdProductId(Integer id);

    List<StockEntry> findByIdInventoryId(Integer id);
}
