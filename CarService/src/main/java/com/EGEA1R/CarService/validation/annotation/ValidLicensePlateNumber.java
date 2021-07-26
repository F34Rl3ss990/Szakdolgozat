package com.EGEA1R.CarService.validation.annotation;

import com.EGEA1R.CarService.validation.classes.licenseplate.LicensePlateCarAdd;
import com.EGEA1R.CarService.validation.classes.licenseplate.LicensePlateCarAndUserDTO;
import com.EGEA1R.CarService.validation.classes.licenseplate.LicensePlateUnauthorizedUserService;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {LicensePlateCarAdd.class, LicensePlateUnauthorizedUserService.class, LicensePlateCarAndUserDTO.class})
@Documented
public @interface ValidLicensePlateNumber {
    String message() default "Invalid license plate number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
