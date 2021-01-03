package com.EGEA1R.CarService.validation;

import com.EGEA1R.CarService.web.DTO.ChangePasswordDTO;

public class PasswordMatchesChangePasswordValidator extends PasswordMatchesValidator<ChangePasswordDTO> {
    @Override
    protected boolean validation(ChangePasswordDTO value) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
