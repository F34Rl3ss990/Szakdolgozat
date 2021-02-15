package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.PasswordMatches;
import com.EGEA1R.CarService.validation.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class PasswordResetDTO {

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    private String token;


}
