package com.car_service.egea1r.web.data.payload.request;

import com.car_service.egea1r.validation.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidTaxNumber(
        billingToCompany = "billingToCompany",
        foreignTaxNumber = "billingEuTax",
        taxNumber = "billingTaxNumber"
)
public class ModifyUserDateRequest {

    private Long userId;

    @NotNull
    private Boolean billingToCompany;

    @ValidName
    private String billingName;

    @NotNull
    @ValidPhoneNumber
    private String billingPhoneNumber;

    @NotNull
    @Min(1000)
    @Max(9999)
    private Integer billingZipCode;

    @NotNull
    @ValidAccentAndWhitespace
    private String billingTown;

    @NotNull
    private String billingStreet;

    private String billingOtherAddressType;

    private String billingTaxNumber;

    @NotNull
    private Boolean billingEuTax;

    @ValidEmail
    private String billingEmail;
}
