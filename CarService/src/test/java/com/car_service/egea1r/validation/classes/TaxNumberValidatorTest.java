package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.validation.annotation.FieldsValueMatch;
import com.car_service.egea1r.validation.annotation.ValidTaxNumber;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import com.car_service.egea1r.web.data.payload.request.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxNumberValidatorTest {

    @Mock
    private ValidTaxNumber validTaxNumber;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenToCompanyTrueAndEuTaxFalse_whenValid_thenCorrect() {
        when(validTaxNumber.taxNumber()).thenReturn("billingTaxNumber");
        when(validTaxNumber.foreignTaxNumber()).thenReturn("billingEuTax");
        when(validTaxNumber.billingToCompany()).thenReturn("billingToCompany");


        TaxNumberValidator taxNumberValidator = new TaxNumberValidator();
        taxNumberValidator.initialize(validTaxNumber);

        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .billingTaxNumber("12345678-1-12")
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingTaxNumber("22345678-1-12")
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .billingTaxNumber("22345678-1-12")
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        assertTrue(taxNumberValidator.isValid(modifyUserDataRequest, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(unauthorizedUserReservationDTO, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(carAndUserDTO, constraintValidatorContext));
    }

    @Test
    void givenToCompanyTrueAndEuTaxFalseAndTaxIsNull_whenValid_thenCorrect() {
        when(validTaxNumber.taxNumber()).thenReturn("billingTaxNumber");
        when(validTaxNumber.foreignTaxNumber()).thenReturn("billingEuTax");
        when(validTaxNumber.billingToCompany()).thenReturn("billingToCompany");


        TaxNumberValidator taxNumberValidator = new TaxNumberValidator();
        taxNumberValidator.initialize(validTaxNumber);

        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .billingTaxNumber(null)
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingTaxNumber(null)
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .billingTaxNumber(null)
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        assertTrue(taxNumberValidator.isValid(modifyUserDataRequest, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(unauthorizedUserReservationDTO, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(carAndUserDTO, constraintValidatorContext));
    }

    @Test
    void givenToCompanyTrueAndEuTaxTrue_whenValid_thenCorrect() {
        when(validTaxNumber.taxNumber()).thenReturn("billingTaxNumber");
        when(validTaxNumber.foreignTaxNumber()).thenReturn("billingEuTax");
        when(validTaxNumber.billingToCompany()).thenReturn("billingToCompany");


        TaxNumberValidator taxNumberValidator = new TaxNumberValidator();
        taxNumberValidator.initialize(validTaxNumber);

        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .billingTaxNumber("145678-1-12")
                .billingToCompany(true)
                .billingEuTax(true)
                .build();

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingTaxNumber("22678-1-12")
                .billingToCompany(true)
                .billingEuTax(true)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .billingTaxNumber("223678-1-12")
                .billingToCompany(true)
                .billingEuTax(true)
                .build();

        assertTrue(taxNumberValidator.isValid(modifyUserDataRequest, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(unauthorizedUserReservationDTO, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(carAndUserDTO, constraintValidatorContext));
    }

    @Test
    void givenToCompanyFalseAndEuTaxFalse_whenValid_thenCorrect() {
        when(validTaxNumber.taxNumber()).thenReturn("billingTaxNumber");
        when(validTaxNumber.foreignTaxNumber()).thenReturn("billingEuTax");
        when(validTaxNumber.billingToCompany()).thenReturn("billingToCompany");


        TaxNumberValidator taxNumberValidator = new TaxNumberValidator();
        taxNumberValidator.initialize(validTaxNumber);

        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .billingTaxNumber("")
                .billingToCompany(false)
                .billingEuTax(false)
                .build();

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingTaxNumber("")
                .billingToCompany(false)
                .billingEuTax(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .billingTaxNumber("")
                .billingToCompany(false)
                .billingEuTax(false)
                .build();

        assertTrue(taxNumberValidator.isValid(modifyUserDataRequest, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(unauthorizedUserReservationDTO, constraintValidatorContext));
        assertTrue(taxNumberValidator.isValid(carAndUserDTO, constraintValidatorContext));
    }


    @Test
    void givenToCompanyTrueAndEuTaxFalse_whenNotValid_thenUnCorrect() {
        when(validTaxNumber.taxNumber()).thenReturn("billingTaxNumber");
        when(validTaxNumber.foreignTaxNumber()).thenReturn("billingEuTax");
        when(validTaxNumber.billingToCompany()).thenReturn("billingToCompany");


        TaxNumberValidator taxNumberValidator = new TaxNumberValidator();
        taxNumberValidator.initialize(validTaxNumber);

        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .billingTaxNumber("145678-1-12")
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingTaxNumber("45678-1-12")
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .billingTaxNumber("45678-1-12")
                .billingToCompany(true)
                .billingEuTax(false)
                .build();

        assertFalse(taxNumberValidator.isValid(modifyUserDataRequest, constraintValidatorContext));
        assertFalse(taxNumberValidator.isValid(unauthorizedUserReservationDTO, constraintValidatorContext));
        assertFalse(taxNumberValidator.isValid(carAndUserDTO, constraintValidatorContext));
    }
}
