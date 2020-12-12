package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.ProductRequestValidationHelper;
import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.Product;
import com.elvison.inventoryapp.model.rest.ProductRequest;
import com.elvison.inventoryapp.repository.CategoryRepository;
import com.elvison.inventoryapp.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService service;
    private ProductRequestValidationHelper validationHelper;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Before
    public void setup() throws Exception {
        validationHelper = Mockito.mock(ProductRequestValidationHelper.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);

        service = Mockito.spy(new ProductService());
        FieldSetter.setField(service, ProductService.class.getDeclaredField("validationHelper"), validationHelper);
        FieldSetter.setField(service, ProductService.class.getDeclaredField("categoryRepository"), categoryRepository);
        FieldSetter.setField(service, ProductService.class.getDeclaredField("productRepository"), productRepository);
    }

    @Test
    public void GIVEN_valid_request_WHEN_create_product_THEN_saves_product() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("One");
        given(request.getCategoryId()).willReturn(1);

        Category category = Mockito.mock(Category.class);

        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(category));
        given(productRepository.existsByName(anyString())).willReturn(false);

        service.createProduct(request);

        verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
        verify(productRepository, times(1)).existsByName(argThat("One"::equals));
        verify(productRepository, times(1)).save(argThat(p -> "One".equals(p.getName()) && p.getCategoryId() == 1));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_request_with_non_existing_category_WHEN_create_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("One");
        given(request.getCategoryId()).willReturn(1);

        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.empty());

        try {
            service.createProduct(request);
        } finally {
            verify(validationHelper, never()).validate(any(), any());
            verify(productRepository, never()).existsByName(any());
            verify(productRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_create_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("One");
        given(request.getCategoryId()).willReturn(1);

        Category category = Mockito.mock(Category.class);

        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(category));
        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any(), any());

        try {
            service.createProduct(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
            verify(productRepository, never()).existsByName(any());
            verify(productRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_duped_name_WHEN_create_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("One");
        given(request.getCategoryId()).willReturn(1);

        Category category = Mockito.mock(Category.class);

        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(category));
        given(productRepository.existsByName(anyString())).willReturn(true);

        try {
            service.createProduct(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
            verify(productRepository, times(1)).existsByName(argThat("One"::equals));
            verify(productRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_valid_name_filter_WHEN_get_products_THEN_find_by_name() {
        service.getProducts("something");

        verify(productRepository, times(1)).findByNameLike(argThat("something"::equals));
        verify(productRepository, never()).findAll();
    }

    @Test
    public void GIVEN_empty_name_filter_WHEN_get_products_THEN_find_by_name() {
        service.getProducts("");

        verify(productRepository, never()).findByNameLike(anyString());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_null_name_filter_WHEN_get_products_THEN_find_by_name() {
        service.getProducts(null);

        verify(productRepository, never()).findByNameLike(anyString());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_id_WHEN_get_product_THEN_find_by_id() {
        service.getProduct(2);

        verify(productRepository, times(1)).findById(intThat(i -> i == 2));
    }

    @Test
    public void GIVEN_valid_request_with_updated_both_name_and_category_WHEN_update_product_THEN_saves_product() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two-New");
        given(request.getCategoryId()).willReturn(2);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);

        Category category = Mockito.mock(Category.class);
        given(category.getId()).willReturn(2);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(categoryRepository.findById(intThat(i -> i == 2))).willReturn(Optional.of(category));
        given(productRepository.existsByName(anyString())).willReturn(false);

        service.updateProduct(10, request);

        verify(productRepository, times(1)).existsByName(argThat("Two-New"::equals));
        verify(product, times(1)).setName(argThat("Two-New"::equals));
        verify(categoryRepository, times(1)).findById(intThat(i -> i == 2));
        verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
        verify(product, times(1)).setCategoryId(intThat(i -> i == 2));
        verify(product, never()).setId(anyInt());
        verify(productRepository, times(1)).save(argThat(product::equals));
    }

    @Test
    public void GIVEN_valid_request_with_updated_name_only_WHEN_update_product_THEN_saves_product() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two-New");
        given(request.getCategoryId()).willReturn(1);

        Category category = Mockito.mock(Category.class);
        given(category.getId()).willReturn(1);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);
        given(product.getCategory()).willReturn(category);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(category));
        given(productRepository.existsByName(anyString())).willReturn(false);

        service.updateProduct(10, request);

        verify(productRepository, times(1)).existsByName(argThat("Two-New"::equals));
        verify(product, times(1)).setName(argThat("Two-New"::equals));
        verify(categoryRepository, never()).findById(anyInt());
        verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
        verify(product, never()).setCategoryId(any());
        verify(product, never()).setId(anyInt());
        verify(productRepository, times(1)).save(argThat(product::equals));
    }

    @Test
    public void GIVEN_valid_request_with_updated_category_only_WHEN_update_product_THEN_saves_product() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two");
        given(request.getCategoryId()).willReturn(2);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);

        Category category = Mockito.mock(Category.class);
        given(category.getId()).willReturn(2);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(categoryRepository.findById(intThat(i -> i == 2))).willReturn(Optional.of(category));

        service.updateProduct(10, request);

        verify(productRepository, never()).existsByName(any());
        verify(product, never()).setName(any());
        verify(categoryRepository, times(1)).findById(intThat(i -> i == 2));
        verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
        verify(product, times(1)).setCategoryId(intThat(i -> i == 2));
        verify(product, never()).setId(anyInt());
        verify(productRepository, times(1)).save(argThat(product::equals));
    }

    @Test
    public void GIVEN_valid_request_with_nothing_updated_WHEN_update_product_THEN_saves_product() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two");
        given(request.getCategoryId()).willReturn(1);

        Category category = Mockito.mock(Category.class);
        given(category.getId()).willReturn(1);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);
        given(product.getCategory()).willReturn(category);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(category));
        given(productRepository.existsByName(anyString())).willReturn(false);

        service.updateProduct(10, request);

        verify(productRepository, never()).existsByName(any());
        verify(product, never()).setName(any());
        verify(categoryRepository, never()).findById(anyInt());
        verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
        verify(product, never()).setCategoryId(any());
        verify(product, never()).setId(anyInt());
        verify(productRepository, times(1)).save(argThat(product::equals));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_request_for_non_existing_product_WHEN_update_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two-New");
        given(request.getCategoryId()).willReturn(2);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.empty());

        try {
            service.updateProduct(10, request);
        } finally {
            verify(productRepository, never()).existsByName(any());
            verify(categoryRepository, never()).findById(anyInt());
            verify(validationHelper, never()).validate(any(), any());
            verify(productRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_with_duped_name_WHEN_update_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two-New");
        given(request.getCategoryId()).willReturn(2);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(productRepository.existsByName(anyString())).willReturn(true);

        try {
            service.updateProduct(10, request);
        } finally {
            verify(productRepository, times(1)).existsByName(argThat("Two-New"::equals));
            verify(product, never()).setName(any());
            verify(categoryRepository, never()).findById(anyInt());
            verify(validationHelper, never()).validate(any(), any());
            verify(product, never()).setCategoryId(any());
            verify(product, never()).setId(anyInt());
            verify(productRepository, never()).save(any());
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_request_with_non_existing_category_WHEN_update_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two-New");
        given(request.getCategoryId()).willReturn(2);

        Category category = Mockito.mock(Category.class);
        given(category.getId()).willReturn(1);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);
        given(product.getCategory()).willReturn(category);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(categoryRepository.findById(intThat(i -> i == 2))).willReturn(Optional.empty());
        given(productRepository.existsByName(anyString())).willReturn(false);

        try {
            service.updateProduct(10, request);
        } finally {
            verify(productRepository, times(1)).existsByName(argThat("Two-New"::equals));
            verify(product, times(1)).setName(argThat("Two-New"::equals));
            verify(categoryRepository, times(1)).findById(intThat(i -> i == 2));
            verify(validationHelper, never()).validate(any(), any());
            verify(product, never()).setCategoryId(anyInt());
            verify(product, never()).setId(anyInt());
            verify(productRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_update_product_THEN_throws_exception() {
        ProductRequest request = Mockito.mock(ProductRequest.class);
        given(request.getName()).willReturn("Two-New");
        given(request.getCategoryId()).willReturn(2);

        Product product = Mockito.mock(Product.class);
        given(product.getName()).willReturn("Two");
        given(product.getCategoryId()).willReturn(1);
        given(product.getId()).willReturn(10);

        Category category = Mockito.mock(Category.class);
        given(category.getId()).willReturn(2);

        given(productRepository.findById(intThat(i -> i == 10))).willReturn(Optional.of(product));
        given(categoryRepository.findById(intThat(i -> i == 2))).willReturn(Optional.of(category));
        given(productRepository.existsByName(anyString())).willReturn(false);

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any(), any());

        try {
            service.updateProduct(10, request);
        } finally {
            verify(productRepository, times(1)).existsByName(argThat("Two-New"::equals));
            verify(product, times(1)).setName(argThat("Two-New"::equals));
            verify(categoryRepository, times(1)).findById(intThat(i -> i == 2));
            verify(validationHelper, times(1)).validate(argThat(request::equals), argThat(category::equals));
            verify(product, times(1)).setCategoryId(intThat(i -> i == 2));
            verify(product, never()).setId(anyInt());
            verify(productRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_existing_id_WHEN_delete_product_THEN_delete_product() {
        given(productRepository.existsById(intThat(i -> i == 1))).willReturn(true);

        service.deleteProduct(1);
        verify(productRepository, times(1)).existsById(intThat(i -> i == 1));
        verify(productRepository, times(1)).deleteById(intThat(i -> i == 1));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_non_existing_id_WHEN_delete_product_THEN_throws_exception() {
        given(productRepository.existsById(intThat(i -> i == 1))).willReturn(false);

        try {
            service.deleteProduct(1);
        } finally {
            verify(productRepository, times(1)).existsById(intThat(i -> i == 1));
            verify(productRepository, never()).deleteById(anyInt());
        }
    }
}
