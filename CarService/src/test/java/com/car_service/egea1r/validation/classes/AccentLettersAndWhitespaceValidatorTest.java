package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.ValidAccentAndWhitespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccentLettersAndWhitespaceValidatorTest {

    @Mock
    private ValidAccentAndWhitespace validAccentAndWhitespace;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenAccentAndWhiteSpaceString_whenValid_thenCorrect(){

        AccentLettersAndWhitespaceValidator accentLettersAndWhitespaceValidator = new AccentLettersAndWhitespaceValidator();
        accentLettersAndWhitespaceValidator.initialize(validAccentAndWhitespace);

        String accentAndWhiteSpaceString = "ááá áá";

        assertTrue(accentLettersAndWhitespaceValidator.isValid(accentAndWhiteSpaceString, constraintValidatorContext));
    }

    @Test
    void givenAccentAndWhiteSpaceString_whenContainsNumber_thenUnCorrect(){

        AccentLettersAndWhitespaceValidator accentLettersAndWhitespaceValidator = new AccentLettersAndWhitespaceValidator();
        accentLettersAndWhitespaceValidator.initialize(validAccentAndWhitespace);

        String accentAndWhiteSpaceString = "ááá á23á";

        assertFalse(accentLettersAndWhitespaceValidator.isValid(accentAndWhiteSpaceString, constraintValidatorContext));
    }

}
