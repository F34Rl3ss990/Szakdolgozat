package com.EGEA1R.CarService.web.DTO.payload.request;

import com.EGEA1R.CarService.validation.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidTaxNumber
public class ModifyUserDateRequest {

    @NotNull
    private Long userId;

    @NotNull
    @ValidPhoneNumber
    private String phoneNumber;

    private Boolean billingToCompany;

    @ValidName
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
