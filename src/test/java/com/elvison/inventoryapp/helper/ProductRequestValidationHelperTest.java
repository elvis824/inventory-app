package com.elvison.inventoryapp.helper;

import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.rest.ProductRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;

public class ProductRequestValidationHelperTest {

    private ProductRequestValidationHelper helper;
    private ProductRequest request;
    private Category category;

    @Before
    public void setup() {
        helper = new ProductRequestValidationHelper();

        request = Mockito.mock(ProductRequest.class);
        category = Mockito.mock(Category.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_empty_request_name_and_good_category_WHEN_validate_THEN_throws_exception() {
        updateMock("", "category");
        helper.validate(request, category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_null_request_name_and_good_category_WHEN_validate_THEN_throws_exception() {
        updateMock(null, "category");
        helper.validate(request, category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_name_with_only_whitespace_and_good_category_WHEN_validate_THEN_throws_exception() {
        updateMock(" ", "category");
        helper.validate(request, category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_name_with_illgeal_char_and_good_category_WHEN_validate_THEN_throws_exception() {
        updateMock("this/that", "category");
        helper.validate(request, category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_name_with_illgeal_char_and_null_category_WHEN_validate_THEN_throws_exception() {
        updateMock("Test-Name 1_2", null);
        helper.validate(request, category);
    }

    @Test
    public void GIVEN_good_request_name_and_good_category_WHEN_validate_THEN_no_errors() {
        updateMock("Test-Name 1_2", "category");
        helper.validate(request, category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_bad_clothes_product_WHEN_validate_THEN_throws_exception() {
        updateMock("Cake", "Clothes");
        helper.validate(request, category);
    }

    @Test
    public void GIVEN_valid_clothes_product_WHEN_validate_THEN_no_errors() {
        updateMock("Dress", "Clothes");
        helper.validate(request, category);

        updateMock("Shirt", "Clothes");
        helper.validate(request, category);

        updateMock("Shoe", "Clothes");
        helper.validate(request, category);

        updateMock("Skirt", "Clothes");
        helper.validate(request, category);

        updateMock("Sock", "Clothes");
        helper.validate(request, category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_bad_food_product_WHEN_validate_THEN_throws_exception() {
        updateMock("Shoe", "Food");
        helper.validate(request, category);
    }

    @Test
    public void GIVEN_valid_food_product_WHEN_validate_THEN_no_errors() {
        updateMock("Bread", "Food");
        helper.validate(request, category);

        updateMock("Cake", "Food");
        helper.validate(request, category);

        updateMock("Macaroni", "Food");
        helper.validate(request, category);

        updateMock("Pizza", "Food");
        helper.validate(request, category);

        updateMock("Salad", "Food");
        helper.validate(request, category);
    }

    private void updateMock(String name, String categoryName) {
        given(request.getName()).willReturn(name);
        given(category.getName()).willReturn(categoryName);
    }

}
