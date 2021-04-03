package com.EGEA1R.CarService.validation.classes.licenseplate;

import com.EGEA1R.CarService.validation.annotation.ValidLicensePlateNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class LicensePlateNumberValidator<Object> implements ConstraintValidator<ValidLicensePlateNumber, Object> {


    @Override
    public void initialize(final ValidLicensePlateNumber constraintAnnotation) {
        //
    }


    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        boolean test = validation(obj);
        return test;
    }

    protected abstract boolean validation(Object obj);
}
