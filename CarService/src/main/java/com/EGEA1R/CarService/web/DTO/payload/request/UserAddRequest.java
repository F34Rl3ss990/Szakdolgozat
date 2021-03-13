package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.ValidAccentAndWhitespace;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidName;
import com.EGEA1R.CarService.validation.annotation.ValidPhoneNumber;
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
public class UserAddRequest {

    @ValidName
    @Size(max = 255)
    @NotNull
    private String name;

    @NotNull
    @ValidPhoneNumber
    private String phoneNumber;

    @ValidName
    @Size(max = 255)
    private String billingName;

    @NotNull
    @ValidPhoneNumber
    private String billingPhoneNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]{4}$")
    private Integer billingZipCode;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z\\s\\-]+$")
    private String billingTown;

    @NotNull
    private String billingStreet;

    private String billingOtherAddressType;

    private String billingTaxNumber;

    private Boolean billingEuTax;

    @ValidEmail
    private String billingEmail;
}
