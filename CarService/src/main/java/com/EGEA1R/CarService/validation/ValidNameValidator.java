package com.EGEA1R.CarService.validation;

import com.EGEA1R.CarService.validation.annotation.ValidName;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidNameValidator implements ConstraintValidator<ValidName, String> {
    private static final String NAME_PATTERN = "((?![0-9<>{}\\\\\\\"/|;:~!?@#$%^=&¿§«»ω⊙¤°℃℉€¥£¢¡®©_+]).)*$";

    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public boolean isValid(final String name, final ConstraintValidatorContext context) {
        return (validateName(name));
    }

    private boolean validateName(final String name) {
        Matcher matcher = PATTERN.matcher(name);
        return matcher.matches();
    }
}
