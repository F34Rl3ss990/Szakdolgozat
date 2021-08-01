package com.car_service.egea1r.web.data.DTO;

import com.car_service.egea1r.validation.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidLicensePlate(
        foreignPlate = "foreignCountryPlate",
        licensePlate = "licensePlateNumber"
)
@ValidTaxNumber(
        billingToCompany = "billingToCompany",
        foreignTaxNumber = "billingEuTax",
        taxNumber = "billingTaxNumber"
)
public class CarAndUserDTO {

    @ValidName
    @Size(max = 255)
    @NotNull
    private String name;

    @ValidEmail
    @NotNull
    private String email;

    @ValidPhoneNumber
    @NotNull
    private String phoneNumber;

    @NotNull
    private Boolean billingToCompany;

    @NotNull
    @ValidName
    @Size(max = 255)
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

    @NotNull
    private String brand;

    @NotNull
    private String type;

    @NotNull
    private String engineType;

    @NotNull
    private String yearOfManufacture;

    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String engineNumber;

    @Size(max = 17)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String chassisNumber;

    private String licensePlateNumber;

    @NotNull
    private Boolean foreignCountryPlate;

    private String mileage;

}
