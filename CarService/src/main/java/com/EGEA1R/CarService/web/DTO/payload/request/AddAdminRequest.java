package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.PasswordMatches;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidPassword;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@PasswordMatches
public class AddAdminRequest {

    @NotBlank
    @Size(max = 255)
    @ValidEmail
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    private String mfa;

}