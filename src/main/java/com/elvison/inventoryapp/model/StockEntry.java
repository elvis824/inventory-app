package com.elvison.inventoryapp.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stocks")
@Schema(description = "Stock Entry")
public class StockEntry {

    @EmbeddedId
    @JsonUnwrapped
    private StockEntryId id;

    @Column(name = "quantity")
    @Schema(required = true, description = "Quantity")
    private Integer quantity;

    public StockEntryId getId() {
        return id;
    }

    public void setId(StockEntryId id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static StockEntry of(Integer productId, Integer inventoryId, Integer quantity) {
        StockEntry entry = new StockEntry();
        entry.setId(StockEntryId.of(productId, inventoryId));
        entry.setQuantity(quantity);
        return entry;
    }
}
