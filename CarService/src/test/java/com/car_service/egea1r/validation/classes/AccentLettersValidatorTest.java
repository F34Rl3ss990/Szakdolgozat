package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidAccentLetters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccentLettersValidatorTest {

    @Mock
    private ValidAccentLetters validAccentLetters;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenAccentString_whenValid_thenCorrect(){

        AccentLettersValidator accentLettersValidator = new AccentLettersValidator();
        accentLettersValidator.initialize(validAccentLetters);

        String accentString = "ááááá";

        assertTrue(accentLettersValidator.isValid(accentString, constraintValidatorContext));
    }

    @Test
    void givenAccentString_whenContainsNumber_thenUnCorrect(){

        AccentLettersValidator accentLettersValidator = new AccentLettersValidator();
        accentLettersValidator.initialize(validAccentLetters);

        String accentString = "ááááá232";

        assertFalse(accentLettersValidator.isValid(accentString, constraintValidatorContext));
    }

    @Test
    void givenAccentString_whenContainsWhiteSpace_thenUnCorrect(){

        AccentLettersValidator accentLettersValidator = new AccentLettersValidator();
        accentLettersValidator.initialize(validAccentLetters);

        String accentString = "ááá áá";

        assertFalse(accentLettersValidator.isValid(accentString, constraintValidatorContext));
    }

}
