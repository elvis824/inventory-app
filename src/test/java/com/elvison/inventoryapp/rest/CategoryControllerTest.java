package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.model.Category;
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
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest extends BaseWebMvcTest {
    @Test
    public void addCategorySuccessTest() throws Exception {
        mvc.perform(post("/v1/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"One\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(categoryService, times(1))
                .createCategory(argThat(r -> "One".equals(r.getName())));
    }

    @Test
    public void getCategoriesWithNameFilterTest() throws Exception {
        List<Category> categories = Collections.singletonList(Category.of(1, "One"));
        given(categoryService.getCategories(argThat("One"::equals)))
                .willReturn(categories);

        mvc.perform(get("/v1/category?name=One"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("One")));

        verify(categoryService, times(1)).getCategories(argThat("One"::equals));
    }

    @Test
    public void getCategoriesWithoutNameFilterTest() throws Exception {
        List<Category> categories = Arrays.asList(
                Category.of(1, "One"),
                Category.of(2, "Two")
        );
        given(categoryService.getCategories(argThat(Objects::isNull)))
                .willReturn(categories);

        mvc.perform(get("/v1/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("One")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Two")));

        verify(categoryService, times(1)).getCategories(argThat(Objects::isNull));
    }

    @Test
    public void getSpecificCategoryFoundTest() throws Exception {
        Category category = Category.of(1, "One");
        given(categoryService.getCategory(intThat(i -> i == 1))).willReturn(Optional.of(category));

        mvc.perform(get("/v1/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("One")));

        verify(categoryService, times(1)).getCategory(intThat(i -> i == 1));
    }

    @Test
    public void getSpecificCategoryNotFoundTest() throws Exception {
        given(categoryService.getCategory(intThat(i -> i == 2))).willReturn(Optional.empty());

        mvc.perform(get("/v1/category/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Category not found")))
                .andExpect(jsonPath("$.timestamp", isA(String.class)));

        verify(categoryService, times(1)).getCategory(intThat(i -> i == 2));
    }

    @Test
    public void updateCategoryTest() throws Exception {
        mvc.perform(patch("/v1/category/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"One\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(categoryService, times(1))
                .updateCategory(
                        intThat(i -> i == 1),
                        argThat(r -> "One".equals(r.getName()))
                );
    }

    @Test
    public void deleteCategoryTest() throws Exception {
        mvc.perform(delete("/v1/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(categoryService, times(1))
                .deleteCategory(intThat(i -> i == 1));
    }
}
