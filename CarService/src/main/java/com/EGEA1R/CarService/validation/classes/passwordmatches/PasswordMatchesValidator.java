package com.EGEA1R.CarService.validation.classes.passwordmatches;

import com.EGEA1R.CarService.validation.annotation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class PasswordMatchesValidator<Object> implements ConstraintValidator<PasswordMatches, Object> {


    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        boolean test = validation(obj);
        return test;
    }

    protected abstract boolean validation(Object obj);
}
