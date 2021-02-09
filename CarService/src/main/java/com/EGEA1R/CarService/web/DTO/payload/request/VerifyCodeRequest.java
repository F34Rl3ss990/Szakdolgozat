package com.EGEA1R.CarService.web.DTO.payload.request;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String email;
    private String code;
}