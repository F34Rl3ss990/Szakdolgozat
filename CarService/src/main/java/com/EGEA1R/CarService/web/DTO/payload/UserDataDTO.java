package com.EGEA1R.CarService.web.DTO.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {

    private String name;
    private String email;
    private String phoneNumber;
    private String billingName;
    private String billingEmail;
    private String billingPhoneNumber;
    private Integer billingZipCode;
    private String billingTown;
    private String billingStreet;
    private String billingOtherAddressType;
    private String billingTaxNumber;


}
