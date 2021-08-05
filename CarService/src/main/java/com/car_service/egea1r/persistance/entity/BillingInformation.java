package com.car_service.egea1r.persistance.entity;

import com.car_service.egea1r.validation.annotation.ValidAccentAndWhitespace;
import com.car_service.egea1r.validation.annotation.ValidEmail;
import com.car_service.egea1r.validation.annotation.ValidPhoneNumber;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BillingInformation that = (BillingInformation) o;

        if (!Objects.equals(billingName, that.billingName)) return false;
        if (!Objects.equals(billingPhoneNumber, that.billingPhoneNumber)) return false;
        if (!Objects.equals(billingZipCode, that.billingZipCode)) return false;
        if (!Objects.equals(billingTown, that.billingTown)) return false;
        if (!Objects.equals(billingStreet, that.billingStreet)) return false;
        if (!Objects.equals(billingOtherAddressType, that.billingOtherAddressType)) return false;
        if (!Objects.equals(billingTaxNumber, that.billingTaxNumber)) return false;
        return Objects.equals(billingEmail, that.billingEmail);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(billingName);
        result = 31 * result + (Objects.hashCode(billingPhoneNumber));
        result = 31 * result + (Objects.hashCode(billingZipCode));
        result = 31 * result + (Objects.hashCode(billingTown));
        result = 31 * result + (Objects.hashCode(billingStreet));
        result = 31 * result + (Objects.hashCode(billingOtherAddressType));
        result = 31 * result + (Objects.hashCode(billingTaxNumber));
        result = 31 * result + (Objects.hashCode(billingEmail));
        return result;
    }
}
