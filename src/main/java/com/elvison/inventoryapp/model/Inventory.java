package com.elvison.inventoryapp.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;

@Entity
@Table(name = "inventories")
@Schema(description = "Inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Inventory ID")
    private Integer id;

    @Column(name = "name")
    @Schema(description = "Inventory Name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Inventory of(Integer id, String name) {
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setName(name);
        return inventory;
    }
}
