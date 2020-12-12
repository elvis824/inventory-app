package com.elvison.inventoryapp.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @Schema(description = "Timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ZZZ")
    private final ZonedDateTime timestamp = ZonedDateTime.now();

    @Schema(description = "Error message")
    private String message;

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public static ApiError of(String message) {
        ApiError error = new ApiError();
        error.message = message;
        return error;
    }
}
