package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.DefaultNameValidationHelper;
import com.elvison.inventoryapp.model.Inventory;
import com.elvison.inventoryapp.model.rest.InventoryRequest;
import com.elvison.inventoryapp.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private DefaultNameValidationHelper validationHelper;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public void createInventory(InventoryRequest request) {
        validationHelper.validate(request);

        if (inventoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Inventory with the same name already exists");
        }

        Inventory inventory = new Inventory();
        inventory.setName(request.getName());
        inventoryRepository.save(inventory);
    }

    public List<Inventory> getInventories(String nameFilter) {
        if (StringUtils.isEmpty(nameFilter)) {
            return inventoryRepository.findAll();
        }
        return inventoryRepository.findByNameLike(nameFilter);
    }

    public Optional<Inventory> getInventory(Integer id) {
        return inventoryRepository.findById(id);
    }

    @Transactional
    public void updateInventory(Integer id, InventoryRequest request) {
        validationHelper.validate(request);

        if (inventoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Inventory with the same name already exists");
        }

        Inventory inventory = inventoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        inventory.setName(request.getName());
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(Integer id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found");
        }
        inventoryRepository.deleteById(id);
    }
}
