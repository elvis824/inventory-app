package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.model.StockEntry;
import com.elvison.inventoryapp.model.rest.ApiError;
import com.elvison.inventoryapp.model.rest.StockEntryRequest;
import com.elvison.inventoryapp.service.StockService;
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
@RequestMapping("v1/stock")
@Tag(name = "Stock", description = "Stock Management")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    @Operation(description = "Creates a new stock entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void addStock(
            @RequestBody
            @Parameter(description = "New stock entry details", required = true)
                    StockEntryRequest request
    ) {
        stockService.createStockEntry(request);
    }

    @GetMapping
    @Operation(description = "Gets stock entries")
    @ApiResponse(responseCode = "200", description = "Success")
    public List<StockEntry> getStockEntries(
            @RequestParam(name = "productId", required = false)
            @Parameter(description = "Product ID")
                    Integer productId,
            @RequestParam(name = "inventoryId", required = false)
            @Parameter(description = "Inventory ID")
                    Integer inventoryId
    ) {
        return stockService.getStockEntries(productId, inventoryId);
    }

    @PatchMapping
    @Operation(description = "Updates an existing stock entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void updateStockEntry(
            @RequestBody
            @Parameter(description = "Update stock entry", required = true)
                    StockEntryRequest request
    ) {
        stockService.updateStockEntry(request);
    }
}
