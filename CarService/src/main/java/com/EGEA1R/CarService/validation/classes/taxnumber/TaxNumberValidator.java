package com.EGEA1R.CarService.validation.classes.taxnumber;

import com.EGEA1R.CarService.validation.annotation.ValidTaxNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class TaxNumberValidator<Object> implements ConstraintValidator<ValidTaxNumber, Object> {

    private final String regexp = "^[0-9]{8}[-][0-9][-][0-9]{2}$";

    @Override
    public void initialize(final ValidTaxNumber constraintAnnotation) {
        //
    }


    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        boolean test = validation(obj, regexp);
        return test;
    }

    protected abstract boolean validation(Object obj, String regexp);
}
