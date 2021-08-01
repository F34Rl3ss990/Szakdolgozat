package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.FieldsValueMatch;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, T> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(final FieldsValueMatch constraintAnnotation){
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(final T object, final ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(object).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(object).getPropertyValue(fieldMatch);

        if(fieldValue != null){
            return fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
    }
}
