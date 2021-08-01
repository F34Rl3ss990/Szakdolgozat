package com.car_service.egea1r.web.data.payload.request;

import com.car_service.egea1r.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    private String password;
}
