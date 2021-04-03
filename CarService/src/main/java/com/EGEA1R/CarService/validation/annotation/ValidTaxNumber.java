package com.EGEA1R.CarService.validation.annotation;

import com.EGEA1R.CarService.validation.classes.taxnumber.TaxNumberUnauthorizedUserReservation;
import com.EGEA1R.CarService.validation.classes.taxnumber.TaxNumberUserAdd;
import com.EGEA1R.CarService.validation.classes.taxnumber.TaxNumberUserModify;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {TaxNumberUserAdd.class, TaxNumberUserModify.class, TaxNumberUnauthorizedUserReservation.class})
@Documented
public @interface ValidTaxNumber {
    String message() default "Invalid tax number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
