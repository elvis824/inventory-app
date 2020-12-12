package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.service.CategoryService;
import com.elvison.inventoryapp.service.InventoryService;
import com.elvison.inventoryapp.service.ProductService;
import com.elvison.inventoryapp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class BaseWebMvcTest {
    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected InventoryService inventoryService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected StockService stockService;

    @MockBean
    protected ProductService productService;
}
