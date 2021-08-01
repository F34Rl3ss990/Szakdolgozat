package com.car_service.egea1r.web.data.payload.request;

import com.car_service.egea1r.validation.annotation.FieldsValueMatch;
import com.car_service.egea1r.validation.annotation.ValidEmail;
import com.car_service.egea1r.validation.annotation.ValidPassword;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@FieldsValueMatch(
        field = "password",
        fieldMatch = "matchingPassword"
)
public class AddAdminRequest {

    @NotBlank
    @Size(max = 255)
    @ValidEmail
    private String email;

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    private String mfa;

}
