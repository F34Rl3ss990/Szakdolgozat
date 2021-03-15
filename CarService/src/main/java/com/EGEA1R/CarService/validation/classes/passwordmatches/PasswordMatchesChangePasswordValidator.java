package com.EGEA1R.CarService.validation.classes.passwordmatches;

import com.EGEA1R.CarService.web.DTO.payload.request.ChangePasswordRequest;

public class PasswordMatchesChangePasswordValidator extends PasswordMatchesValidator<ChangePasswordRequest> {
    @Override
    protected boolean validation(ChangePasswordRequest value) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
