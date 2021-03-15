package com.EGEA1R.CarService.validation.classes.passwordmatches;


import com.EGEA1R.CarService.web.DTO.payload.request.PasswordResetRequest;



public class PasswordMatchesPasswordresetValidator extends PasswordMatchesValidator<PasswordResetRequest> {
    @Override
    protected boolean validation(PasswordResetRequest value) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
