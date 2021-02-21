package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
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
    @Pattern(regexp = "^[a-zA-Z]+$")
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
    @Pattern(regexp = "[A-Za-z0-9]")
    private String engineNumber;

    @Size(max = 17)
    @Pattern(regexp = "[A-Za-z0-9]")
    private String chassisNumber;

    @Size(max = 10)
    @NotNull
    private String licensePlateNumber;

    private Boolean foreignCountryPlate;

    @Max(9999999)
    @PositiveOrZero
    @NotNull
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
    private String phoneNumber;


    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(max = 11)
    private String billingPhoneNumber;

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

    @Size(max = 1000)
    private String comment;

    @NotNull
    @FutureOrPresent
    private LocalDate reservedDate;

    private List<ReservedServiceList> service = new ArrayList<>();
}


