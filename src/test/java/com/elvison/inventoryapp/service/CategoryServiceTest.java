package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.DefaultNameValidationHelper;
import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.rest.CategoryRequest;
import com.elvison.inventoryapp.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryService service;
    private DefaultNameValidationHelper validationHelper;
    private CategoryRepository categoryRepository;

    @Before
    public void setup() throws Exception {
        validationHelper = Mockito.mock(DefaultNameValidationHelper.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);

        service = Mockito.spy(new CategoryService());
        FieldSetter.setField(service, CategoryService.class.getDeclaredField("validationHelper"), validationHelper);
        FieldSetter.setField(service, CategoryService.class.getDeclaredField("categoryRepository"), categoryRepository);
    }

    @Test
    public void GIVEN_valid_request_WHEN_create_category_THEN_saves_category() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);
        given(request.getName()).willReturn("One");

        given(categoryRepository.existsByName(anyString())).willReturn(false);

        service.createCategory(request);

        verify(validationHelper, times(1)).validate(argThat(request::equals));
        verify(categoryRepository, times(1)).existsByName(argThat("One"::equals));
        verify(categoryRepository, times(1)).save(argThat(c -> "One".equals(c.getName())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_create_category_THEN_throws_exception() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any());

        try {
            service.createCategory(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(categoryRepository, never()).existsByName(any());
            verify(categoryRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_duped_request_WHEN_create_category_THEN_throws_exception() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);
        given(request.getName()).willReturn("One");

        given(categoryRepository.existsByName(anyString())).willReturn(true);

        try {
            service.createCategory(request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(categoryRepository, times(1)).existsByName(argThat("One"::equals));
            verify(categoryRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_valid_name_filter_WHEN_get_categories_THEN_find_by_name() {
        service.getCategories("something");

        verify(categoryRepository, times(1)).findByNameLike(argThat("something"::equals));
        verify(categoryRepository, never()).findAll();
    }

    @Test
    public void GIVEN_empty_name_filter_WHEN_get_categories_THEN_find_by_name() {
        service.getCategories("");

        verify(categoryRepository, never()).findByNameLike(anyString());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_null_name_filter_WHEN_get_categories_THEN_find_by_name() {
        service.getCategories(null);

        verify(categoryRepository, never()).findByNameLike(anyString());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void GIVEN_id_WHEN_get_category_THEN_find_by_id() {
        service.getCategory(2);

        verify(categoryRepository, times(1)).findById(intThat(i -> i == 2));
    }

    @Test
    public void GIVEN_valid_request_WHEN_update_category_THEN_saves_category() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);
        given(request.getName()).willReturn("One");

        Category category = Mockito.mock(Category.class);

        given(categoryRepository.existsByName(anyString())).willReturn(false);
        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.of(category));

        service.updateCategory(1, request);

        verify(validationHelper, times(1)).validate(argThat(request::equals));
        verify(categoryRepository, times(1)).existsByName(argThat("One"::equals));
        verify(categoryRepository, times(1)).findById(intThat(i -> i == 1));
        verify(category, times(1)).setName(argThat("One"::equals));
        verify(categoryRepository, times(1)).save(argThat(category::equals));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_invalid_request_WHEN_update_category_THEN_throws_exception() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);
        given(request.getName()).willReturn("One");

        doThrow(IllegalArgumentException.class)
                .when(validationHelper).validate(any());

        try {
            service.updateCategory(1, request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(categoryRepository, never()).existsByName(any());
            verify(categoryRepository, never()).findById(anyInt());
            verify(categoryRepository, never()).save(any());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_duped_request_WHEN_update_category_THEN_throws_exception() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);
        given(request.getName()).willReturn("One");

        given(categoryRepository.existsByName(anyString())).willReturn(true);

        try {
            service.updateCategory(1, request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(categoryRepository, times(1)).existsByName(argThat("One"::equals));
            verify(categoryRepository, never()).findById(anyInt());
            verify(categoryRepository, never()).save(any());
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_request_for_non_existing_WHEN_update_category_THEN_throws_exception() {
        CategoryRequest request = Mockito.mock(CategoryRequest.class);
        given(request.getName()).willReturn("One");


        given(categoryRepository.existsByName(anyString())).willReturn(false);
        given(categoryRepository.findById(intThat(i -> i == 1))).willReturn(Optional.empty());

        try {
            service.updateCategory(1, request);
        } finally {
            verify(validationHelper, times(1)).validate(argThat(request::equals));
            verify(categoryRepository, times(1)).existsByName(argThat("One"::equals));
            verify(categoryRepository, times(1)).findById(intThat(i -> i == 1));
            verify(categoryRepository, never()).save(any());
        }
    }

    @Test
    public void GIVEN_existing_id_WHEN_delete_category_THEN_delete_category() {
        given(categoryRepository.existsById(intThat(i -> i == 1))).willReturn(true);

        service.deleteCategory(1);
        verify(categoryRepository, times(1)).existsById(intThat(i -> i == 1));
        verify(categoryRepository, times(1)).deleteById(intThat(i -> i == 1));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GIVEN_non_existing_id_WHEN_delete_category_THEN_throws_exception() {
        given(categoryRepository.existsById(intThat(i -> i == 1))).willReturn(false);

        try {
            service.deleteCategory(1);
        } finally {
            verify(categoryRepository, times(1)).existsById(intThat(i -> i == 1));
            verify(categoryRepository, never()).deleteById(anyInt());
        }
    }
}
