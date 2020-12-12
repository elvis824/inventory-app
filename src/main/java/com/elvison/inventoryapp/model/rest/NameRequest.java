package com.elvison.inventoryapp.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;

public class NameRequest {
    @Schema(required = true, description = "Name")
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
