package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VerifyCodeRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String code;
    @NotBlank
    private String password;
}