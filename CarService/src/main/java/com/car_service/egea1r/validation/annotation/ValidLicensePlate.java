package com.car_service.egea1r.validation.annotation;

import com.car_service.egea1r.validation.classes.LicensePlateNumberValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LicensePlateNumberValidator.class)
public @interface ValidLicensePlate {

    String message() default "Invalid input";

    String licensePlate();

    String foreignPlate();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidLicensePlate[] value();
    }
}
