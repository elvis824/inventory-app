package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.model.Inventory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(InventoryController.class)
public class InventoryControllerTest extends BaseWebMvcTest {
    @Test
    public void addInventorySuccessTest() throws Exception {
        mvc.perform(post("/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"One\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(inventoryService, times(1))
                .createInventory(argThat(r -> "One".equals(r.getName())));
    }

    @Test
    public void getInventoriesWithNameFilterTest() throws Exception {
        List<Inventory> inventories = Collections.singletonList(Inventory.of(1, "One"));
        given(inventoryService.getInventories(argThat("One"::equals)))
                .willReturn(inventories);

        mvc.perform(get("/v1/inventory?name=One"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("One")));

        verify(inventoryService, times(1)).getInventories(argThat("One"::equals));
    }

    @Test
    public void getInventoriesWithoutNameFilterTest() throws Exception {
        List<Inventory> inventories = Arrays.asList(
                Inventory.of(1, "One"),
                Inventory.of(2, "Two")
        );
        given(inventoryService.getInventories(argThat(Objects::isNull)))
                .willReturn(inventories);

        mvc.perform(get("/v1/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("One")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Two")));

        verify(inventoryService, times(1)).getInventories(argThat(Objects::isNull));
    }

    @Test
    public void getSpecificInventoryFoundTest() throws Exception {
        Inventory inventory = Inventory.of(1, "One");
        given(inventoryService.getInventory(intThat(i -> i == 1))).willReturn(Optional.of(inventory));

        mvc.perform(get("/v1/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("One")));

        verify(inventoryService, times(1)).getInventory(intThat(i -> i == 1));
    }

    @Test
    public void getSpecificInventoryNotFoundTest() throws Exception {
        given(inventoryService.getInventory(intThat(i -> i == 2))).willReturn(Optional.empty());

        mvc.perform(get("/v1/inventory/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Inventory not found")))
                .andExpect(jsonPath("$.timestamp", isA(String.class)));

        verify(inventoryService, times(1)).getInventory(intThat(i -> i == 2));
    }

    @Test
    public void updateInventoryTest() throws Exception {
        mvc.perform(patch("/v1/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"One\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(inventoryService, times(1))
                .updateInventory(
                        intThat(i -> i == 1),
                        argThat(r -> "One".equals(r.getName()))
                );
    }

    @Test
    public void deleteInventoryTest() throws Exception {
        mvc.perform(delete("/v1/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(inventoryService, times(1))
                .deleteInventory(intThat(i -> i == 1));
    }
}
