package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NameValidatorTest {

    @Mock
    private ValidName validName;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenName_whenValid_thenCorrect() {

        NameValidator nameValidator = new NameValidator();
        nameValidator.initialize(validName);

        String name = "Joska";

        assertTrue(nameValidator.isValid(name, constraintValidatorContext));
    }

    @Test
    void givenName_whenNotValid_thenUnCorrect() {

        NameValidator nameValidator = new NameValidator();
        nameValidator.initialize(validName);

        String nameNumber = "Jóska22";
        String nameSpecial = "Jóska|";

        assertFalse(nameValidator.isValid(nameNumber, constraintValidatorContext));
        assertFalse(nameValidator.isValid(nameSpecial, constraintValidatorContext));
    }
}
