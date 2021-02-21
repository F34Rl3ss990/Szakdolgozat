package com.EGEA1R.CarService.persistance.entity;

import com.EGEA1R.CarService.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BillingInformation {


    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(max = 11)
    private String phoneNumber;

    @NotNull
    private String country;

    @NotNull
    private Integer zipCode;

    @NotNull
    private String town;

    @NotNull
    private String street;

    private String otherAddressType;

    private String taxNumber;

    @ValidEmail
    private String email;
}
