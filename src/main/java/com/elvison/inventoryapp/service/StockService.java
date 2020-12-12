package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.StockRequestValidationHelper;
import com.elvison.inventoryapp.model.StockEntry;
import com.elvison.inventoryapp.model.StockEntryId;
import com.elvison.inventoryapp.model.rest.StockEntryRequest;
import com.elvison.inventoryapp.repository.InventoryRepository;
import com.elvison.inventoryapp.repository.ProductRepository;
import com.elvison.inventoryapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StockRequestValidationHelper validationHelper;

    @Transactional
    public void createStockEntry(StockEntryRequest request) {
        validationHelper.validate(request);

        StockEntryId id = StockEntryId.of(request.getProductId(), request.getInventoryId());
        if (stockRepository.existsById(id)) {
            throw new IllegalArgumentException(String.format("Stock entry for given product ID %d and inventory ID %d already exists", request.getProductId(), request.getInventoryId()));
        }

        if (!productRepository.existsById(request.getProductId())) {
            throw new IllegalArgumentException(String.format("Product of ID %d does not exist", request.getProductId()));
        }
        if (!inventoryRepository.existsById(request.getInventoryId())) {
            throw new IllegalArgumentException(String.format("Inventory of ID %d does not exist", request.getInventoryId()));
        }

        StockEntry entry = new StockEntry();
        entry.setId(id);
        entry.setQuantity(request.getQuantity());
        stockRepository.save(entry);
    }

    public List<StockEntry> getStockEntries(Integer productId, Integer inventoryId) {
        if (inventoryId != null && productId != null) {
            return stockRepository
                    .findById(StockEntryId.of(productId, inventoryId))
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }
        if (inventoryId != null) {
            return stockRepository.findByIdInventoryId(inventoryId);
        }
        if (productId != null) {
            return stockRepository.findByIdProductId(productId);
        }
        return stockRepository.findAll();
    }

    @Transactional
    public void updateStockEntry(StockEntryRequest request) {
        validationHelper.validate(request);

        StockEntryId id = StockEntryId.of(request.getProductId(), request.getInventoryId());
        StockEntry entry = stockRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Stock entry for given product ID %d and inventory ID %d is not found", request.getProductId(), request.getInventoryId())));
        entry.setQuantity(request.getQuantity());
        stockRepository.save(entry);
    }
}
