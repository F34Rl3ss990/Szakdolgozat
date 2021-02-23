package com.EGEA1R.CarService.validation.annotation;

import com.EGEA1R.CarService.validation.AccentLettersAndWhitespaceValidator;
import com.EGEA1R.CarService.validation.AccentLettersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AccentLettersAndWhitespaceValidator.class)
@Documented
public @interface ValidAccentAndWhitespace {
    String message() default "Invalid name input";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}