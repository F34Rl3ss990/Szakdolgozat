package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnauthorizedUserReservationDTO {


    @Size(max = 30)
    @ValidAccentAndWhitespace
    @NotNull
    private String brand;

    @Size(max = 30)
    @NotNull
    private String type;

    @Size(max = 30)
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

    @Size(max = 10)
    private String licensePlateNumber;

    private Boolean foreignCountryPlate;

    @Max(9999999)
    @PositiveOrZero
    private Integer mileage;

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

    @Size(max = 1000)
    private String comment;

    @NotNull
    private Date reservedDate;

    @NotNull
    private List<String> reservedServices = new ArrayList<>();
}


