package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.model.Inventory;
import com.elvison.inventoryapp.model.rest.ApiError;
import com.elvison.inventoryapp.model.rest.InventoryRequest;
import com.elvison.inventoryapp.service.InventoryService;
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
@RequestMapping("v1/inventory")
@Tag(name = "Inventory", description = "Inventory Management")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    @Operation(description = "Creates a new inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void addInventory(
            @RequestBody
            @Parameter(description = "New inventory details", required = true)
                    InventoryRequest request
    ) {
        inventoryService.createInventory(request);
    }

    @GetMapping
    @Operation(description = "Gets inventory details")
    @ApiResponse(responseCode = "200", description = "Success")
    public List<Inventory> getInventories(
            @RequestParam(name = "name", required = false)
            @Parameter(description = "Inventory name filter")
                    String nameFilter
    ) {
        return inventoryService.getInventories(nameFilter);
    }

    @GetMapping(value = "/{inventoryId}")
    @Operation(description = "Gets detail for given inventory ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Inventory getInventory(
            @PathVariable
            @Parameter(description = "Inventory ID")
                    Integer inventoryId
    ) {
        return inventoryService
                .getInventory(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
    }

    @PatchMapping(value = "/{inventoryId}")
    @Operation(description = "Updates an existing inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void updateInventory(
            @PathVariable
            @Parameter(description = "Inventory ID")
                    Integer inventoryId,
            @RequestBody
            @Parameter(description = "Update inventory details", required = true)
                    InventoryRequest request
    ) {
        inventoryService.updateInventory(inventoryId, request);
    }

    @DeleteMapping(value = "/{inventoryId}")
    @Operation(description = "Deletes an existing inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void deleteInventory(
            @PathVariable
            @Parameter(description = "Inventory ID")
                    Integer inventoryId
    ) {
        inventoryService.deleteInventory(inventoryId);
    }
}
