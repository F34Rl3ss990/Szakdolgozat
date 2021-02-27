package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.ValidAccentAndWhitespace;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest {

    private String firstName;

    private String lastName;

    @NotNull
    @ValidPhoneNumber
    private String phoneNumber;

    @ValidAccentAndWhitespace
    private String billingName;

    @NotNull
    @ValidPhoneNumber
    private String billingPhoneNumber;

    @ValidAccentAndWhitespace
    private String billingCountry;

    private Integer billingZipCode;

    @Pattern(regexp = "^[a-zA-Z/s]+$")
    private String billingTown;

    private String billingStreet;

    private String billingOtherAddressType;

    private String billingTaxNumber;

    private Boolean billingEuTax;

    @ValidEmail
    private String billingEmail;
}
