package com.EGEA1R.CarService.validation;


import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;

public class PasswordMatchesAddAdminRequest extends PasswordMatchesValidator<AddAdminRequest> {
    @Override
    protected boolean validation(AddAdminRequest value) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}