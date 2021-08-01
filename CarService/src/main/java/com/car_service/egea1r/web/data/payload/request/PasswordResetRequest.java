package com.car_service.egea1r.web.data.payload.request;

import com.car_service.egea1r.validation.annotation.FieldsValueMatch;
import com.car_service.egea1r.validation.annotation.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldsValueMatch(
        field = "password",
        fieldMatch = "matchingPassword"
)
public class PasswordResetRequest {

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    private String token;


}
