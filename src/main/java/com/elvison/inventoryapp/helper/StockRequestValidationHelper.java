package com.elvison.inventoryapp.helper;

import com.elvison.inventoryapp.model.rest.StockEntryRequest;
import org.springframework.stereotype.Component;

@Component
public class StockRequestValidationHelper {

    public void validate(StockEntryRequest request) {
        if (request.getInventoryId() == null) {
            throw new IllegalArgumentException("Inventory ID is invalid");
        }

        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Product ID is invalid");
        }

        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity is invalid");
        }
    }
}
