package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordConstraintValidatorTest {

    @Mock
    private ValidPassword validPassword;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;


    @Test
    void givenPassword_whenValid_thenCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "Abswtz126.";

        assertTrue(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

    @Test
    void givenPassword_whenNotContainUppercaseLetter_thenUnCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "ubswtz126.";

        assertFalse(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

    @Test
    void givenPassword_whenNotContainNumber_thenUnCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "UBSWTZasd.";

        assertFalse(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

    @Test
    void givenPassword_whenNotContainSpecialCharacter_thenUnCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "UBSWTZasd";

        assertFalse(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

    @Test
    void givenPassword_whenLengthIsUnder8Character_thenUnCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "Ub.2";

        assertFalse(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

    @Test
    void givenPassword_whenLengthIsAbove30Character_thenUnCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "Ub1s3423ffsdf23423fdsf23423fsdf34534fdgdf32432gfdgsfsdafdsa..dd";

        assertFalse(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

    @Test
    void givenPassword_whenContainWhiteSpace_thenUnCorrect(){

        PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
        passwordConstraintValidator.initialize(validPassword);

        String password = "Ub2.sdasgf fsd";

        assertFalse(passwordConstraintValidator.isValid(password, constraintValidatorContext));
    }

}


