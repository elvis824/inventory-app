package com.elvison.inventoryapp.helper;

import com.elvison.inventoryapp.model.rest.StockEntryRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;

public class StockRequestValidationHelperTest {

    private StockRequestValidationHelper helper;
    private StockEntryRequest request;

    @Before
    public void setup() {
        helper = new StockRequestValidationHelper();
        request = Mockito.mock(StockEntryRequest.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_null_inventory_WHEN_validate_THEN_throws_exception() {
        updateMock(null, 2, 3);
        helper.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_null_product_WHEN_validate_THEN_throws_exception() {
        updateMock(1, null, 3);
        helper.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_null_quantity_WHEN_validate_THEN_throws_exception() {
        updateMock(1, 2, null);
        helper.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_negative_quantity_WHEN_validate_THEN_throws_exception() {
        updateMock(1, 2, -2);
        helper.validate(request);
    }

    @Test
    public void GIVEN_request_with_zero_quantity_WHEN_validate_THEN_no_errors() {
        updateMock(1, 2, 0);
        helper.validate(request);
    }

    @Test
    public void GIVEN_all_request_WHEN_validate_THEN_no_errors() {
        updateMock(1, 2, 3);
        helper.validate(request);
    }

    private void updateMock(Integer inventoryId, Integer productId, Integer quantity) {
        given(request.getInventoryId()).willReturn(inventoryId);
        given(request.getProductId()).willReturn(productId);
        given(request.getQuantity()).willReturn(quantity);
    }
}
