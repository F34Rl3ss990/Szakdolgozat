package com.EGEA1R.CarService.controller.DTO;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@PasswordMatches
public class ChangePasswordDTO {

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    private String oldPassword;


}
