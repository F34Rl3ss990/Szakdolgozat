package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidTaxNumber
@ValidLicensePlateNumber
public class UnauthorizedUserReservationDTO {

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

    private Boolean foreignCountryPlate;

    private String mileage;

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

    private Boolean billingEuTax;

    @ValidEmail
    private String billingEmail;

    @Size(max = 1000)
    private String comment;

    @NotNull
    @FutureOrPresent
    private Date reservedDate;

    @NotNull
    private String reservedServices;
}


