package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.ValidAccentAndWhitespace;
import com.EGEA1R.CarService.validation.annotation.ValidAccentLetters;
import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import com.EGEA1R.CarService.validation.annotation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.*;
import java.time.LocalDate;
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

    @ValidAccentLetters
    @Size(max = 60)
    @NotNull
    private String firstName;

    @ValidAccentLetters
    @Size(max = 40)
    @NotNull
    private String lastName;

    @ValidEmail
    @NotNull
    private String email;

    @ValidPhoneNumber
    @NotNull
    private String phoneNumber;

    @NotNull
    private String billingName;

    @NotNull
    @ValidPhoneNumber
    private String billingPhoneNumber;

    @NotNull
    @ValidAccentAndWhitespace
    private String billingCountry;

    @NotNull
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
    private Date reservedDate;

    private List<ReservedServiceList> reservedServices = new ArrayList<>();
}


