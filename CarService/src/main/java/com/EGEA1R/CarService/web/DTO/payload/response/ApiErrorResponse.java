package com.EGEA1R.CarService.web.DTO.payload.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Builder
@Data
public class ApiErrorResponse {

    private HttpStatus status;
    private String message;
    private List<String> errors;
    

    public ApiErrorResponse() {
        super();
    }

    public ApiErrorResponse(final HttpStatus status, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiErrorResponse(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }


    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    public void setError(final String error) {
        errors = Arrays.asList(error);
    }

}
