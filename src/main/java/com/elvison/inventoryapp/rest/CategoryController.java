package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.rest.ApiError;
import com.elvison.inventoryapp.model.rest.CategoryRequest;
import com.elvison.inventoryapp.service.CategoryService;
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
@RequestMapping("v1/category")
@Tag(name = "Category", description = "Category Management")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(description = "Creates a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void addCategory(
            @RequestBody
            @Parameter(description = "New category details", required = true)
                    CategoryRequest request
    ) {
        categoryService.createCategory(request);
    }

    @GetMapping
    @Operation(description = "Gets category details")
    @ApiResponse(responseCode = "200", description = "Success")
    public List<Category> getCategories(
            @RequestParam(name = "name", required = false)
            @Parameter(description = "Category name filter")
                    String nameFilter
    ) {
        return categoryService.getCategories(nameFilter);
    }

    @GetMapping(value = "/{categoryId}")
    @Operation(description = "Gets detail for given category ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Category getCategory(
            @PathVariable
            @Parameter(description = "Category ID")
                    int categoryId
    ) {
        return categoryService
                .getCategory(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @PatchMapping(value = "/{categoryId}")
    @Operation(description = "Updates an existing category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void updateCategory(
            @PathVariable
            @Parameter(description = "Category ID")
                    Integer categoryId,
            @RequestBody
            @Parameter(description = "Update category details", required = true)
                    CategoryRequest request
    ) {
        categoryService.updateCategory(categoryId, request);
    }

    @DeleteMapping(value = "/{categoryId}")
    @Operation(description = "Deletes an existing category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void deleteCategory(
            @PathVariable
            @Parameter(description = "Category ID")
                    Integer categoryId
    ) {
        categoryService.deleteCategory(categoryId);
    }
}
