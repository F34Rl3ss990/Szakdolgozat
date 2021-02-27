package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.ValidAccentAndWhitespace;
import com.EGEA1R.CarService.validation.annotation.ValidAccentLetters;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDateRequest {

    @NotNull
    private Long userId;

    @NotNull
    @ValidPhoneNumber
    private String phoneNumber;

    @ValidAccentAndWhitespace
    private String billingName;

    @NotNull
    @ValidPhoneNumber
    private String billingPhoneNumber;

    @ValidAccentLetters
    private String billingCountry;

    private Integer billingZipCode;

    @ValidAccentAndWhitespace
    private String billingTown;

    private String billingStreet;

    private String billingOtherAddressType;

    private String billingTaxNumber;

    private Boolean billingEuTax;

    @ValidEmail
    private String billingEmail;
}
