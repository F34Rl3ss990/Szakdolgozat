package com.EGEA1R.CarService.validation;

import com.EGEA1R.CarService.web.DTO.payload.request.SignupRequest;

public class PasswordMatchesSignupValidator extends PasswordMatchesValidator<SignupRequest> {
    @Override
    protected boolean validation(SignupRequest value) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
