package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDataDTO {

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @Pattern(regexp = "^{11}[0-9]")
    @Size(max = 11)
    private Integer phoneNumber;

    private String name;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(max = 11)
    private Integer billingPhoneNumber;

    @Pattern(regexp = "^[a-zA-Z/s]+$")
    private String country;

    private Integer zipCode;

    @Pattern(regexp = "^[a-zA-Z/s]+$")
    private String town;

    private String street;

    private String otherAddressType;

    private String taxNumber;

    private Boolean euTax;

    @ValidEmail
    private String billingEmail;
}
