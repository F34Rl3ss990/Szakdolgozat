package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.PasswordMatches;
import com.EGEA1R.CarService.validation.annotation.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class ChangePasswordRequest {

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    private String oldPassword;

    @NotNull
    private String otpNum;


}
