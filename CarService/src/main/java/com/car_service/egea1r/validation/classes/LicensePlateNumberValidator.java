package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidLicensePlate;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class LicensePlateNumberValidator  implements ConstraintValidator<ValidLicensePlate, T> {
    private String licensePlate;
    private String foreignLicensePlate;

    @Override
    public void initialize(final ValidLicensePlate constraintAnnotation){
        this.licensePlate = constraintAnnotation.licensePlate();
        this.foreignLicensePlate = constraintAnnotation.foreignPlate();
    }

    @Override
    public boolean isValid(final T object, final ConstraintValidatorContext context) {
        String licenseValue = (String) new BeanWrapperImpl(object).getPropertyValue(licensePlate);
        boolean booleanValue = Boolean.parseBoolean((new BeanWrapperImpl(object).getPropertyValue(String.valueOf(foreignLicensePlate))).toString());
        if(licenseValue!=null) {
            if (licenseValue.equals("")) {
                return true;
            }  else if (!licenseValue.equals("") && !booleanValue) {
                return patternMatcher(licenseValue);
            }
        }
        return true;
    }

    private boolean patternMatcher(String licensePlateNumber) {
        return (Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", licensePlateNumber) ||
                Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", licensePlateNumber) ||
                Pattern.matches("^[/p/P][-][0-9]{5}$", licensePlateNumber) ||
                Pattern.matches("^[a-zA-z]{3}[0-9]{5}", licensePlateNumber));
    }
}
