package com.car_service.egea1r.validation.annotation;

import com.car_service.egea1r.validation.classes.AccentLettersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = AccentLettersValidator.class)
public @interface ValidAccentLetters {
    String message() default "Input contains invalid characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
