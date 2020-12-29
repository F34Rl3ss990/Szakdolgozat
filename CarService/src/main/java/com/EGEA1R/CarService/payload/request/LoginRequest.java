package com.EGEA1R.CarService.payload.request;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginRequest {

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    private String password;
}
