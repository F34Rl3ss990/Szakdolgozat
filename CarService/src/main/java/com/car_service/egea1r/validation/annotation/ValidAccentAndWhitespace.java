package com.car_service.egea1r.validation.annotation;

import com.car_service.egea1r.validation.classes.AccentLettersAndWhitespaceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = AccentLettersAndWhitespaceValidator.class)
public @interface ValidAccentAndWhitespace {
    String message() default "Input contains invalid characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
