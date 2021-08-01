package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidTaxNumber;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TaxNumberValidator implements ConstraintValidator<ValidTaxNumber, T> {

    private final String regexp = "^[0-9]{8}[-][0-9][-][0-9]{2}$";
    private String taxNumber;
    private String foreignTaxNumber;
    private String billingToCompany;


    @Override
    public void initialize(final ValidTaxNumber constraintAnnotation) {
        this.taxNumber = constraintAnnotation.taxNumber();
        this.foreignTaxNumber = constraintAnnotation.foreignTaxNumber();
        this.billingToCompany = constraintAnnotation.billingToCompany();
    }

    @Override
    public boolean isValid(final T object, final ConstraintValidatorContext context) {
        String taxValue = (String) new BeanWrapperImpl(object).getPropertyValue(taxNumber);
        boolean booleanValue = Boolean.parseBoolean((new BeanWrapperImpl(object).getPropertyValue(String.valueOf(foreignTaxNumber))).toString());
        boolean companyTaxValue = Boolean.parseBoolean((new BeanWrapperImpl(object).getPropertyValue(String.valueOf(billingToCompany))).toString());
        if (companyTaxValue && taxValue != null) {
            if (booleanValue) {
                return true;
            } else {
                return Pattern.matches(regexp, taxValue);
            }
        } else if (!companyTaxValue) {
            return true;
        }
        return true;
    }
}
