package com.elvison.inventoryapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Schema(description = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Product ID")
    private Integer id;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "name")
    @Schema(description = "Product Name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private transient Category category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public static Product of(Integer id, Integer categoryId, String name) {
        Product product = new Product();
        product.setId(id);
        product.setCategoryId(categoryId);
        product.setName(name);
        return product;
    }
}
