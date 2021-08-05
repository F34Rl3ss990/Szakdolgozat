package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {

    @Mock
    private ValidEmail validEmail;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenEmail_whenValid_thenCorrect(){

        EmailValidator emailValidator = new EmailValidator();
        emailValidator.initialize(validEmail);

        String email = "asd@asd.hu";

        assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void givenEmail_whenFirstPartNotValid_thenUnCorrect(){

        EmailValidator emailValidator = new EmailValidator();
        emailValidator.initialize(validEmail);

        String email = "as!23,.d@asd.hu";

        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void givenEmail_whenMissingAtSign_thenUnCorrect(){

        EmailValidator emailValidator = new EmailValidator();
        emailValidator.initialize(validEmail);

        String email = "asdd.asd.hu";

        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void givenEmail_whenThirdPartNotValid_thenUnCorrect(){

        EmailValidator emailValidator = new EmailValidator();
        emailValidator.initialize(validEmail);

        String email = "asdd@asd.hu!23";

        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void givenEmail_whenThirdPartShort_thenUnCorrect(){

        EmailValidator emailValidator = new EmailValidator();
        emailValidator.initialize(validEmail);

        String email = "asdd@asd.h";

        assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

}
