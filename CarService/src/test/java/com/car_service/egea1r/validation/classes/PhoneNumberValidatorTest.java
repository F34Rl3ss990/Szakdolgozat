package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidPhoneNumber;
import com.car_service.egea1r.validation.annotation.ValidTaxNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneNumberValidatorTest {

    @Mock
    private ValidPhoneNumber validPhoneNumber;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenPhoneNumber_whenValid_thenCorrect() {

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();
        phoneNumberValidator.initialize(validPhoneNumber);

        String phoneNumber = "06701234567";

        assertTrue(phoneNumberValidator.isValid(phoneNumber, constraintValidatorContext));
    }

    @Test
    void givenPhoneNumber_whenLengthUnder8Number_thenUnCorrect() {

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();
        phoneNumberValidator.initialize(validPhoneNumber);

        String phoneNumberShort = "012";

        assertFalse(phoneNumberValidator.isValid(phoneNumberShort, constraintValidatorContext));
    }

    @Test
    void givenPhoneNumber_whenLengthAbove14Number_thenUnCorrect() {

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();
        phoneNumberValidator.initialize(validPhoneNumber);

        String phoneNumberLong = "06701234232323232323232567";

        assertFalse(phoneNumberValidator.isValid(phoneNumberLong, constraintValidatorContext));
    }

    @Test
    void givenPhoneNumber_whenContainsLetter_thenUnCorrect() {

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();
        phoneNumberValidator.initialize(validPhoneNumber);

        String phoneNumberWithLetter = "06701234567as";

        assertFalse(phoneNumberValidator.isValid(phoneNumberWithLetter, constraintValidatorContext));
    }

    @Test
    void givenPhoneNumber_whenContainsSpecialCharacter_thenUnCorrect() {

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();
        phoneNumberValidator.initialize(validPhoneNumber);

        String phoneNumberWithSpecialChar = "06 70 123-1234";

        assertFalse(phoneNumberValidator.isValid(phoneNumberWithSpecialChar, constraintValidatorContext));
    }
}
