package com.EGEA1R.CarService.validation.annotation;

import com.EGEA1R.CarService.validation.PasswordMatchesChangePasswordValidator;
import com.EGEA1R.CarService.validation.PasswordMatchesPasswordresetValidator;
import com.EGEA1R.CarService.validation.PasswordMatchesSignupValidator;
import com.EGEA1R.CarService.validation.PasswordMatchesValidator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {PasswordMatchesSignupValidator.class, PasswordMatchesPasswordresetValidator.class, PasswordMatchesChangePasswordValidator.class})
@Documented
public @interface PasswordMatches {

    String message() default "Passwords don't match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
