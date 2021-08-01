package com.car_service.egea1r.web.data.payload.request;

import com.car_service.egea1r.validation.annotation.FieldsValueMatch;
import com.car_service.egea1r.validation.annotation.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldsValueMatch(
        field = "password",
        fieldMatch = "matchingPassword"
)
public class ChangePasswordRequest {

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String otpNum;


}
