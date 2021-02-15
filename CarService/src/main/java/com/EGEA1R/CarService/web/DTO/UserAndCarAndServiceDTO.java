package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAndCarAndServiceDTO {

    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "brand")
    private String brand;

    @Size(max = 30)
    @Column(name = "type")
    private String type;

    @Size(max = 30)
    @Column(name = "engine_type")
    private String engineType;

    @Min(1900)
    @PastOrPresent
    @Positive
    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;

    @Size(max = 30)
    @Pattern(regexp = "[A-Za-z0-9]")
    @Column(name = "engine_number")
    private String engineNumber;

    @Size(max = 17)
    @Pattern(regexp = "[A-Za-z0-9]")
    @Column(name = "chassis_number")
    private String chassisNumber;

    @Max(9999999)
    @PositiveOrZero
    @Column(name = "mileage")
    private Integer mileage;

    @Pattern(regexp = "[a-zA-Z]")
    @Size(max = 60)
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]")
    @Size(max = 40)
    private String lastName;

    @ValidEmail
    private String email;

    @Pattern(regexp = "^{11}[0-9]")
    @Size(max = 11)
    private Integer phoneNumber;

    @Size(max = 1000)
    private String comment;

    @Size(max = 10)
    private String licensePlateNumber;

    private boolean foreignCountryPlate;

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(max = 11)
    private Integer billingPhoneNumber;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z/s]+$")
    private String country;

    @NotNull
    private Integer zipCode;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z/s]+$")
    private String town;

    @NotNull
    private String street;

    @NotNull
    private String otherAddressType;

    private String taxNumber;

    private Boolean euTax;

    @ValidEmail
    private String billingEmail;
}


