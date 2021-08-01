package com.car_service.egea1r.persistance.entity;

import com.car_service.egea1r.validation.annotation.ValidAccentAndWhitespace;
import com.car_service.egea1r.validation.annotation.ValidEmail;
import com.car_service.egea1r.validation.annotation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BillingInformation {

    @NotNull
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

    @ValidEmail
    private String billingEmail;
}
