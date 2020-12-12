package com.elvison.inventoryapp.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product Request Data")
public class ProductRequest extends NameRequest {
    @Schema(required = true, description = "Category ID")
    private Integer categoryId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
