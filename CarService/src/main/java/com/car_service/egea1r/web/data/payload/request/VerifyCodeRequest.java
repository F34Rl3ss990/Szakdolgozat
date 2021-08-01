package com.car_service.egea1r.web.data.payload.request;

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
