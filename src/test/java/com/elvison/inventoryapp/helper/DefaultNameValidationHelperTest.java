package com.elvison.inventoryapp.helper;

import com.elvison.inventoryapp.model.rest.NameRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;

public class DefaultNameValidationHelperTest {

    private DefaultNameValidationHelper helper;
    private NameRequest request;

    @Before
    public void setup() {
        helper = new DefaultNameValidationHelper();
        request = Mockito.mock(NameRequest.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_empty_request_name_WHEN_validate_THEN_throws_exception() {
        updateMock("");
        helper.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_null_request_name_WHEN_validate_THEN_throws_exception() {
        updateMock(null);
        helper.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_name_with_only_whitespace_WHEN_validate_THEN_throws_exception() {
        updateMock(" ");
        helper.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GIVEN_request_name_with_illgeal_char_WHEN_validate_THEN_throws_exception() {
        updateMock("this/that");
        helper.validate(request);
    }

    @Test
    public void GIVEN_good_request_name_WHEN_validate_THEN_no_errors() {
        updateMock("Test-Name 1_2");
        helper.validate(request);
    }

    private void updateMock(String name) {
        given(request.getName()).willReturn(name);
    }
}
