package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.FieldsValueMatch;
import com.car_service.egea1r.web.data.payload.request.AddAdminRequest;
import com.car_service.egea1r.web.data.payload.request.ChangePasswordRequest;
import com.car_service.egea1r.web.data.payload.request.PasswordResetRequest;
import com.car_service.egea1r.web.data.payload.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldsValueMatchValidatorTest {

    @Mock
    private FieldsValueMatch fieldsValueMatch;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenPasswordAndMatchingPassword_whenValid_thenCorrect() {

        when(fieldsValueMatch.field()).thenReturn("password");
        when(fieldsValueMatch.fieldMatch()).thenReturn("matchingPassword");


        FieldsValueMatchValidator fieldsValueMatchValidator = new FieldsValueMatchValidator();
        fieldsValueMatchValidator.initialize(fieldsValueMatch);

        SignupRequest signupRequest = SignupRequest.builder()
                .password("Adfqet123.")
                .matchingPassword("Adfqet123.")
                .build();
        AddAdminRequest addAdminRequest = AddAdminRequest.builder()
                .password("Adfgets123.")
                .matchingPassword("Adfgets123.")
                .build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .password("NEWgets123.")
                .matchingPassword("NEWgets123.")
                .build();
        PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                .password("NEWgets353.")
                .matchingPassword("NEWgets353.")
                .build();

        assertTrue(fieldsValueMatchValidator.isValid(signupRequest, constraintValidatorContext));
        assertTrue(fieldsValueMatchValidator.isValid(addAdminRequest, constraintValidatorContext));
        assertTrue(fieldsValueMatchValidator.isValid(changePasswordRequest, constraintValidatorContext));
        assertTrue(fieldsValueMatchValidator.isValid(passwordResetRequest, constraintValidatorContext));
    }

    @Test
    void givenNullPassword_whenValid_thenCorrect() {

        when(fieldsValueMatch.field()).thenReturn("password");
        when(fieldsValueMatch.fieldMatch()).thenReturn("matchingPassword");


        FieldsValueMatchValidator fieldsValueMatchValidator = new FieldsValueMatchValidator();
        fieldsValueMatchValidator.initialize(fieldsValueMatch);

        SignupRequest signupRequest = SignupRequest.builder()
                .password(null)
                .matchingPassword("Adfqet123.")
                .build();
        AddAdminRequest addAdminRequest = AddAdminRequest.builder()
                .password(null)
                .matchingPassword("Adfgets123.")
                .build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .password(null)
                .matchingPassword("NEWgets123.")
                .build();
        PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                .password(null)
                .matchingPassword("NEWgets353.")
                .build();

        assertFalse(fieldsValueMatchValidator.isValid(signupRequest, constraintValidatorContext));
        assertFalse(fieldsValueMatchValidator.isValid(addAdminRequest, constraintValidatorContext));
        assertFalse(fieldsValueMatchValidator.isValid(changePasswordRequest, constraintValidatorContext));
        assertFalse(fieldsValueMatchValidator.isValid(passwordResetRequest, constraintValidatorContext));
    }

    @Test
    void givenPasswordAndMatchingPassword_whenNotValid_thenUnCorrect() {

        when(fieldsValueMatch.field()).thenReturn("password");
        when(fieldsValueMatch.fieldMatch()).thenReturn("matchingPassword");


        FieldsValueMatchValidator fieldsValueMatchValidator = new FieldsValueMatchValidator();
        fieldsValueMatchValidator.initialize(fieldsValueMatch);

        SignupRequest signupRequest = SignupRequest.builder()
                .password("Adfqet123.")
                .matchingPassword("Adfqet12")
                .build();
        AddAdminRequest addAdminRequest = AddAdminRequest.builder()
                .password("Adfgets123.")
                .matchingPassword("Adfgets12")
                .build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .password("NEWgets123.")
                .matchingPassword("NEWgets12")
                .build();
        PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                .password("NEWgets353.")
                .matchingPassword("NEWgets35")
                .build();

        assertFalse(fieldsValueMatchValidator.isValid(signupRequest, constraintValidatorContext));
        assertFalse(fieldsValueMatchValidator.isValid(addAdminRequest, constraintValidatorContext));
        assertFalse(fieldsValueMatchValidator.isValid(changePasswordRequest, constraintValidatorContext));
        assertFalse(fieldsValueMatchValidator.isValid(passwordResetRequest, constraintValidatorContext));
    }
}
