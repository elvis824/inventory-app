package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.StockRequestValidationHelper;
import com.elvison.inventoryapp.model.StockEntry;
import com.elvison.inventoryapp.model.rest.StockEntryRequest;
import com.elvison.inventoryapp.repository.InventoryRepository;
import com.elvison.inventoryapp.repository.ProductRepository;
import com.elvison.inventoryapp.repository.StockRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class StockServiceTest {
    private StockService service;
    private StockRequestValidationHelper validationHelper;
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;
    private StockRepository stockRepository;

    @Before
    public void setup() throws Exception {
        validationHelper = Mockito.mock(StockRequestValidationHelper.class);
        productRepository = Mockito.mock(ProductRepository.class);
        inventoryRepository = Mockito.mock(InventoryRepository.class);
        stockRepository = Mockito.mock(StockRepository.class);

        service = Mockito.spy(new StockService());
        FieldSetter.setField(service, StockService.class.getDeclaredField("validationHelper"), validationHelper);
        FieldSetter.setField(service, StockService.class.getDeclaredField("productRepository"), productRepository);
        FieldSetter.setField(service, StockService.class.getDeclaredField("inventoryRepository"), inventoryRepository);
        FieldSetter.setField(service, StockService.class.getDeclaredField("stockRepository"), stockRepository);
    }

    @Test
    public void GIVEN_valid_request_WHEN_create_stock_entry_THEN_saves_stock_entry() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        given(stockRepository.existsById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10))).willReturn(false);
        given(productRepository.existsById(intThat(i -> i == 10))).willReturn(true);
        given(inventoryRepository.existsById(intThat(i -> i == 1))).willReturn(true);

        service.createStockEntry(request);
        verify(validationHelper, times(1)).validate(argThat(request::equals));
        verify(stockRepository, times(1)).save(argThat(s -> s.getQuantity() == 100 && s.getId().getProductId() == 10 && s.getId().getInventoryId() == 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_create_stock_entry_THEN_throws_exception() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any());

        try {
            service.createStockEntry(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(productRepository, never()).existsById(anyInt());
            verify(inventoryRepository, never()).existsById(anyInt());
            verify(stockRepository, never()).existsById(any());
            verify(stockRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_existing_entry_WHEN_create_stock_entry_THEN_throws_exception() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        given(stockRepository.existsById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10))).willReturn(true);
        given(productRepository.existsById(intThat(i -> i == 10))).willReturn(true);
        given(inventoryRepository.existsById(intThat(i -> i == 1))).willReturn(true);

        try {
            service.createStockEntry(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(stockRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_non_existing_product_WHEN_create_stock_entry_THEN_throws_exception() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        given(stockRepository.existsById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10))).willReturn(false);
        given(productRepository.existsById(intThat(i -> i == 10))).willReturn(false);
        given(inventoryRepository.existsById(intThat(i -> i == 1))).willReturn(true);

        try {
            service.createStockEntry(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(stockRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_non_existing_inventory_WHEN_create_stock_entry_THEN_throws_exception() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        given(stockRepository.existsById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10))).willReturn(false);
        given(productRepository.existsById(intThat(i -> i == 10))).willReturn(true);
        given(inventoryRepository.existsById(intThat(i -> i == 1))).willReturn(false);

        try {
            service.createStockEntry(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(stockRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_both_product_id_and_inventory_id_WHEN_get_stock_entries_and_record_found_THEN_finds_results() {
        given(stockRepository.findById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10)))
                .willReturn(Optional.of(Mockito.mock(StockEntry.class)));

        List<StockEntry> entries = service.getStockEntries(10, 1);
        Assert.assertEquals(1, entries.size());
        verify(stockRepository, times(1)).findById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10));
    }

    @Test
    public void GIVEN_both_product_id_and_inventory_id_WHEN_get_stock_entries_and_no_record_found_THEN_returns_empty_list() {
        given(stockRepository.findById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10)))
                .willReturn(Optional.empty());

        List<StockEntry> entries = service.getStockEntries(10, 1);
        Assert.assertEquals(0, entries.size());
        verify(stockRepository, times(1)).findById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10));
    }

    @Test
    public void GIVEN_only_product_id_WHEN_get_stock_entries_THEN_finds_results() {
        List<StockEntry> list = Mockito.mock(List.class);
        given(stockRepository.findByIdProductId(intThat(i -> i == 10)))
                .willReturn(list);

        List<StockEntry> entries = service.getStockEntries(10, null);
        Assert.assertEquals(list, entries);

        verify(stockRepository, times(1)).findByIdProductId(intThat(i -> i == 10));
    }

    @Test
    public void GIVEN_only_inventory_id_WHEN_get_stock_entries_THEN_finds_results() {
        List<StockEntry> list = Mockito.mock(List.class);
        given(stockRepository.findByIdInventoryId(intThat(i -> i == 1)))
                .willReturn(list);

        List<StockEntry> entries = service.getStockEntries(null, 1);
        Assert.assertEquals(list, entries);

        verify(stockRepository, times(1)).findByIdInventoryId(intThat(i -> i == 1));
    }

    @Test
    public void GIVEN_neither_product_id_nor_inventory_id_WHEN_get_stock_entries_THEN_finds_results() {
        List<StockEntry> list = Mockito.mock(List.class);
        given(stockRepository.findAll())
                .willReturn(list);

        List<StockEntry> entries = service.getStockEntries(null, null);
        Assert.assertEquals(list, entries);

        verify(stockRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_valid_request_WHEN_update_stock_entry_THEN_saves_stock_entry() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        StockEntry entry = Mockito.mock(StockEntry.class);
        given(stockRepository.findById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10))).willReturn(Optional.of(entry));

        service.updateStockEntry(request);
        verify(validationHelper, times(1)).validate(argThat(request::equals));
        verify(entry, times(1)).setQuantity(intThat(i -> i == 100));
        verify(stockRepository, times(1)).save(argThat(entry::equals));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_update_stock_entry_THEN_throws_exception() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any());

        try {
            service.updateStockEntry(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(stockRepository, never()).findById(any());
            verify(stockRepository, never()).save(any());
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_valid_request_for_non_existing_entry_WHEN_update_stock_entry_THEN_throws_exception() {
        StockEntryRequest request = Mockito.mock(StockEntryRequest.class);
        given(request.getInventoryId()).willReturn(1);
        given(request.getProductId()).willReturn(10);
        given(request.getQuantity()).willReturn(100);

        given(stockRepository.findById(argThat(id -> id.getInventoryId() == 1 && id.getProductId() == 10))).willReturn(Optional.empty());

        try {
            service.updateStockEntry(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(stockRepository, never()).save(any());
        }
    }
}
