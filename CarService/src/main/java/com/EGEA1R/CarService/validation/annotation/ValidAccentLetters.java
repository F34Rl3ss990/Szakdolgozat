package com.EGEA1R.CarService.validation.annotation;

import com.EGEA1R.CarService.validation.AccentLettersValidator;
import com.EGEA1R.CarService.validation.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AccentLettersValidator.class)
@Documented
public @interface ValidAccentLetters {
    String message() default "Invalid name input";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}