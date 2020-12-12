package com.elvison.inventoryapp.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stock Entry Request Data")
public class StockEntryRequest {

    @Schema(required = true, description = "Product ID")
    private Integer productId;

    @Schema(required = true, description = "Inventory ID")
    private Integer inventoryId;

    @Schema(required = true, description = "Quantity")
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
