package com.EGEA1R.CarService.validation;


import com.EGEA1R.CarService.web.DTO.PasswordResetDTO;



public class PasswordMatchesPasswordresetValidator extends PasswordMatchesValidator<PasswordResetDTO> {
    @Override
    protected boolean validation(PasswordResetDTO value) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
