package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.PasswordMatches;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidPassword;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@PasswordMatches
public class SignupRequest {

    @NotBlank(message = "Email is required")
    @ValidEmail(message = " Invalid Email")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword(message = "Password must contain 1 upper case letter, 1 number, 1 special character and it should be at least 8 character long!")
    private String password;

    @NotBlank
    private String matchingPassword;
}
