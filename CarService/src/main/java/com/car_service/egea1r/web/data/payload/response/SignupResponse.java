package com.car_service.egea1r.web.data.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {

    private boolean mfa;

    private String secretImageUri;
}
