package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.DefaultNameValidationHelper;
import com.elvison.inventoryapp.model.Inventory;
import com.elvison.inventoryapp.model.rest.InventoryRequest;
import com.elvison.inventoryapp.repository.InventoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {
    private InventoryService service;
    private DefaultNameValidationHelper validationHelper;
    private InventoryRepository inventoryRepository;

    @Before
    public void setup() throws Exception {
        validationHelper = Mockito.mock(DefaultNameValidationHelper.class);
        inventoryRepository = Mockito.mock(InventoryRepository.class);

        service = Mockito.spy(new InventoryService());
        FieldSetter.setField(service, InventoryService.class.getDeclaredField("validationHelper"), validationHelper);
        FieldSetter.setField(service, InventoryService.class.getDeclaredField("inventoryRepository"), inventoryRepository);
    }

    @Test
    public void GIVEN_valid_request_WHEN_create_inventory_THEN_saves_inventory() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);
        given(request.getName()).willReturn("One");

        given(inventoryRepository.existsByName(anyString())).willReturn(false);

        service.createInventory(request);

        verify(validationHelper, times(1)).validate(argThat(request::equals));
        verify(inventoryRepository, times(1)).existsByName(argThat("One"::equals));
        verify(inventoryRepository, times(1)).save(argThat(c -> "One".equals(c.getName())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_create_inventory_THEN_throws_exception() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any());

        try {
            service.createInventory(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(inventoryRepository, never()).existsByName(any());
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_duped_request_WHEN_create_inventory_THEN_throws_exception() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);
        given(request.getName()).willReturn("One");

        given(inventoryRepository.existsByName(anyString())).willReturn(true);

        try {
            service.createInventory(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(inventoryRepository, times(1)).existsByName(argThat("One"::equals));
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_valid_name_filter_WHEN_get_inventories_THEN_find_by_name() {
        service.getInventories("something");

        verify(inventoryRepository, times(1)).findByNameLike(argThat("something"::equals));
        verify(inventoryRepository, never()).findAll();
    }

    @Test
    public void GIVEN_empty_name_filter_WHEN_get_inventories_THEN_find_by_name() {
        service.getInventories("");

        verify(inventoryRepository, never()).findByNameLike(anyString());
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_null_name_filter_WHEN_get_inventories_THEN_find_by_name() {
        service.getInventories(null);

        verify(inventoryRepository, never()).findByNameLike(anyString());
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_id_WHEN_get_inventory_THEN_find_by_id() {
        service.getInventory(2);

        verify(inventoryRepository, times(1)).findById(intThat(i -> i == 2));
    }

    @Test
    public void GIVEN_valid_request_WHEN_update_inventory_THEN_saves_inventory() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);
        given(request.getName()).willReturn("One");

        Inventory inventory = Mockito.mock(Inventory.class);

        given(inventoryRepository.existsByName(anyString())).willReturn(false);
        given(inventoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(inventory));

        service.updateInventory(1, request);

        verify(validationHelper, times(1)).validate(argThat(request::equals));
        verify(inventoryRepository, times(1)).existsByName(argThat("One"::equals));
        verify(inventoryRepository, times(1)).findById(intThat(i -> i == 1));
        verify(inventory, times(1)).setName(argThat("One"::equals));
        verify(inventoryRepository, times(1)).save(argThat(inventory::equals));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_update_inventory_THEN_throws_exception() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);
        given(request.getName()).willReturn("One");

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any());

        try {
            service.updateInventory(1, request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(inventoryRepository, never()).existsByName(any());
            verify(inventoryRepository, never()).findById(anyInt());
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_duped_request_WHEN_update_inventory_THEN_throws_exception() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);
        given(request.getName()).willReturn("One");

        given(inventoryRepository.existsByName(anyString())).willReturn(true);

        try {
            service.updateInventory(1, request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(inventoryRepository, times(1)).existsByName(argThat("One"::equals));
            verify(inventoryRepository, never()).findById(anyInt());
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_request_for_non_existing_WHEN_update_inventory_THEN_throws_exception() {
        InventoryRequest request = Mockito.mock(InventoryRequest.class);
        given(request.getName()).willReturn("One");


        given(inventoryRepository.existsByName(anyString())).willReturn(false);
        given(inventoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.empty());

        try {
            service.updateInventory(1, request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(inventoryRepository, times(1)).existsByName(argThat("One"::equals));
            verify(inventoryRepository, times(1)).findById(intThat(i -> i == 1));
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_existing_id_WHEN_delete_inventory_THEN_delete_inventory() {
        given(inventoryRepository.existsById(intThat(i -> i == 1))).willReturn(true);

        service.deleteInventory(1);
        verify(inventoryRepository, times(1)).existsById(intThat(i -> i == 1));
        verify(inventoryRepository, times(1)).deleteById(intThat(i -> i == 1));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_non_existing_id_WHEN_delete_inventory_THEN_throws_exception() {
        given(inventoryRepository.existsById(intThat(i -> i == 1))).willReturn(false);

        try {
            service.deleteInventory(1);
        } finally {
            verify(inventoryRepository, times(1)).existsById(intThat(i -> i == 1));
            verify(inventoryRepository, never()).deleteById(anyInt());
        }
    }
}
