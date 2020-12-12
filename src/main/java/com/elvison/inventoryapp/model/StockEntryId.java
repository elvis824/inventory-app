package com.elvison.inventoryapp.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StockEntryId implements Serializable {

    @Column(name = "product_id")
    @Schema(required = true, description = "Product ID")
    private Integer productId;

    @Column(name = "inventory_id")
    @Schema(required = true, description = "Inventory ID")
    private Integer inventoryId;

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

    public static StockEntryId of(Integer productId, Integer inventoryId) {
        StockEntryId id = new StockEntryId();
        id.setProductId(productId);
        id.setInventoryId(inventoryId);
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEntryId that = (StockEntryId) o;
        return productId.equals(that.productId) && inventoryId.equals(that.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, inventoryId);
    }
}
