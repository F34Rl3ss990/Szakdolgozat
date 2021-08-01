package com.car_service.egea1r.validation.annotation;

import com.car_service.egea1r.validation.classes.LicensePlateNumberValidator;
import com.car_service.egea1r.validation.classes.TaxNumberValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TaxNumberValidator.class)
public @interface ValidTaxNumber {

    String message() default "Invalid input";

    String taxNumber();

    String foreignTaxNumber();

    String billingToCompany();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidTaxNumber[] value();
    }
}
