package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.model.Product;
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
@WebMvcTest(ProductController.class)
public class ProductControllerTest extends BaseWebMvcTest {
    @Test
    public void addProductSuccessTest() throws Exception {
        mvc.perform(post("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"categoryId\":1,\"name\":\"One\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(productService, times(1))
                .createProduct(argThat(r -> r.getCategoryId() == 1 && "One".equals(r.getName())));
    }

    @Test
    public void getProductsWithNameFilterTest() throws Exception {
        List<Product> products = Collections.singletonList(Product.of(1, 1, "One"));
        given(productService.getProducts(argThat("One"::equals)))
                .willReturn(products);

        mvc.perform(get("/v1/product?name=One"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].categoryId", is(1)))
                .andExpect(jsonPath("$[0].name", is("One")));

        verify(productService, times(1)).getProducts(argThat("One"::equals));
    }

    @Test
    public void getProductsWithoutNameFilterTest() throws Exception {
        List<Product> products = Arrays.asList(
                Product.of(1, 1, "One"),
                Product.of(2, 1, "Two")
        );
        given(productService.getProducts(argThat(Objects::isNull)))
                .willReturn(products);

        mvc.perform(get("/v1/product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].categoryId", is(1)))
                .andExpect(jsonPath("$[0].name", is("One")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].categoryId", is(1)))
                .andExpect(jsonPath("$[1].name", is("Two")));

        verify(productService, times(1)).getProducts(argThat(Objects::isNull));
    }

    @Test
    public void getSpecificProductFoundTest() throws Exception {
        Product product = Product.of(1, 1, "One");
        given(productService.getProduct(intThat(i -> i == 1))).willReturn(Optional.of(product));

        mvc.perform(get("/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.categoryId", is(1)))
                .andExpect(jsonPath("$.name", is("One")));

        verify(productService, times(1)).getProduct(intThat(i -> i == 1));
    }

    @Test
    public void getSpecificProductNotFoundTest() throws Exception {
        given(productService.getProduct(intThat(i -> i == 2))).willReturn(Optional.empty());

        mvc.perform(get("/v1/product/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Product not found")))
                .andExpect(jsonPath("$.timestamp", isA(String.class)));

        verify(productService, times(1)).getProduct(intThat(i -> i == 2));
    }

    @Test
    public void updateProductTest() throws Exception {
        mvc.perform(patch("/v1/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"categoryId\":1,\"name\":\"One\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(productService, times(1))
                .updateProduct(
                        intThat(i -> i == 1),
                        argThat(r -> r.getCategoryId() == 1 && "One".equals(r.getName()))
                );
    }

    @Test
    public void deleteProductTest() throws Exception {
        mvc.perform(delete("/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(productService, times(1))
                .deleteProduct(intThat(i -> i == 1));
    }
}
