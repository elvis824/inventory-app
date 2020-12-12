package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.model.StockEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StockController.class)
public class StockControllerTest extends BaseWebMvcTest {
    @Test
    public void addStockEntrySuccessTest() throws Exception {
        mvc.perform(post("/v1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":1,\"inventoryId\":2,\"quantity\":100}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(stockService, times(1))
                .createStockEntry(argThat(r -> r.getProductId() == 1 && r.getInventoryId() == 2 && r.getQuantity() == 100));
    }

    @Test
    public void getStockEntriesWithBothProductIdAndInventoryIdTest() throws Exception {
        List<StockEntry> entries = Collections.singletonList(StockEntry.of(1, 2, 100));
        given(stockService.getStockEntries(intThat(i -> i == 1), intThat(i -> i == 2)))
                .willReturn(entries);

        mvc.perform(get("/v1/stock?productId=1&inventoryId=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].inventoryId", is(2)))
                .andExpect(jsonPath("$[0].quantity", is(100)));

        verify(stockService, times(1)).getStockEntries(intThat(i -> i == 1), intThat(i -> i == 2));
    }

    @Test
    public void getStockEntriesWithOnlyProductIdTest() throws Exception {
        List<StockEntry> entries = Arrays.asList(
                StockEntry.of(1, 2, 100),
                StockEntry.of(1, 3, 500)
        );
        given(stockService.getStockEntries(intThat(i -> i == 1), intThat(Objects::isNull)))
                .willReturn(entries);

        mvc.perform(get("/v1/stock?productId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].inventoryId", is(2)))
                .andExpect(jsonPath("$[0].quantity", is(100)))
                .andExpect(jsonPath("$[1].productId", is(1)))
                .andExpect(jsonPath("$[1].inventoryId", is(3)))
                .andExpect(jsonPath("$[1].quantity", is(500)));

        verify(stockService, times(1)).getStockEntries(intThat(i -> i == 1), intThat(Objects::isNull));
    }

    @Test
    public void getStockEntriesWithOnlyInventoryIdTest() throws Exception {
        List<StockEntry> entries = Arrays.asList(
                StockEntry.of(1, 3, 100),
                StockEntry.of(2, 3, 500)
        );
        given(stockService.getStockEntries(intThat(Objects::isNull), intThat(i -> i == 3)))
                .willReturn(entries);

        mvc.perform(get("/v1/stock?inventoryId=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].inventoryId", is(3)))
                .andExpect(jsonPath("$[0].quantity", is(100)))
                .andExpect(jsonPath("$[1].productId", is(2)))
                .andExpect(jsonPath("$[1].inventoryId", is(3)))
                .andExpect(jsonPath("$[1].quantity", is(500)));

        verify(stockService, times(1)).getStockEntries(intThat(Objects::isNull), intThat(i -> i == 3));
    }

    @Test
    public void getStockEntriesWithNoProductIdAndNoInventoryIdTest() throws Exception {
        List<StockEntry> entries = Arrays.asList(
                StockEntry.of(1, 2, 100),
                StockEntry.of(1, 3, 500),
                StockEntry.of(2, 3, 200)
        );
        given(stockService.getStockEntries(intThat(Objects::isNull), intThat(Objects::isNull)))
                .willReturn(entries);

        mvc.perform(get("/v1/stock"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].inventoryId", is(2)))
                .andExpect(jsonPath("$[0].quantity", is(100)))
                .andExpect(jsonPath("$[1].productId", is(1)))
                .andExpect(jsonPath("$[1].inventoryId", is(3)))
                .andExpect(jsonPath("$[1].quantity", is(500)))
                .andExpect(jsonPath("$[2].productId", is(2)))
                .andExpect(jsonPath("$[2].inventoryId", is(3)))
                .andExpect(jsonPath("$[2].quantity", is(200)));

        verify(stockService, times(1)).getStockEntries(intThat(Objects::isNull), intThat(Objects::isNull));
    }


    @Test
    public void updateStockEntryTest() throws Exception {
        mvc.perform(patch("/v1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":1,\"inventoryId\":2,\"quantity\":100}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(stockService, times(1))
                .updateStockEntry(argThat(r -> r.getProductId() == 1 && r.getInventoryId() == 2 && r.getQuantity() == 100));
    }

}
