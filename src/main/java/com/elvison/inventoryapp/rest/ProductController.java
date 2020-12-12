package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.model.Product;
import com.elvison.inventoryapp.model.rest.ApiError;
import com.elvison.inventoryapp.model.rest.ProductRequest;
import com.elvison.inventoryapp.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/product")
@Tag(name = "Product", description = "Product Management")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(description = "Creates a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void addProduct(
            @RequestBody
            @Parameter(description = "New product details", required = true)
                    ProductRequest request
    ) {
        productService.createProduct(request);
    }

    @GetMapping
    @Operation(description = "Gets product details")
    @ApiResponse(responseCode = "200", description = "Success")
    public List<Product> getProducts(
            @RequestParam(name = "name", required = false)
            @Parameter(description = "Product name filter")
                    String nameFilter
    ) {
        return productService.getProducts(nameFilter);
    }

    @GetMapping(value = "/{productId}")
    @Operation(description = "Gets detail for given product ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Product getProduct(
            @PathVariable
            @Parameter(description = "Product ID")
                    Integer productId
    ) {
        return productService
                .getProduct(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @PatchMapping(value = "/{productId}")
    @Operation(description = "Updates an existing product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void updateProduct(
            @PathVariable
            @Parameter(description = "Product ID")
                    Integer productId,
            @RequestBody
            @Parameter(description = "Update product details", required = true)
                    ProductRequest request
    ) {
        productService.updateProduct(productId, request);
    }

    @DeleteMapping(value = "/{productId}")
    @Operation(description = "Deletes an existing product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void deleteProduct(
            @PathVariable
            @Parameter(description = "Product ID")
                    Integer productId
    ) {
        productService.deleteProduct(productId);
    }

}
