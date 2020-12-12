package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.exception.InternalServerException;
import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestExceptionHandler.class)
public class RestExceptionHandlerTest extends BaseWebMvcTest {

    @Test
    public void GIVEN_any_query_WHEN_resource_not_found_exception_thrown_THEN_returns_404_status() throws Exception {
        given(inventoryService.getInventories(any())).willThrow(new ResourceNotFoundException("the error message"));

        mvc.perform(get("/v1/inventory"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("the error message")))
                .andExpect(jsonPath("$.timestamp", isA(String.class)));
    }

    @Test
    public void GIVEN_any_query_WHEN_internal_server_exception_thrown_THEN_returns_500_status() throws Exception {
        given(inventoryService.getInventories(any())).willThrow(new InternalServerException("the error message"));

        mvc.perform(get("/v1/inventory"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("the error message")))
                .andExpect(jsonPath("$.timestamp", isA(String.class)));
    }

    @Test
    public void GIVEN_any_query_WHEN_illegal_argument_exception_thrown_THEN_returns_400_status() throws Exception {
        given(inventoryService.getInventories(any())).willThrow(new IllegalArgumentException("the error message"));

        mvc.perform(get("/v1/inventory"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("the error message")))
                .andExpect(jsonPath("$.timestamp", isA(String.class)));
    }
}
