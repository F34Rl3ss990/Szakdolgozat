package com.car_service.egea1r.validation.classes;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.validation.annotation.ValidLicensePlate;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.DTO.CarDTO;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicensePlateNumberValidatorTest {

    @Mock
    private ValidLicensePlate validLicensePlate;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void givenLicensePlateNumberFirstPattern_whenValid_thenCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("abc-123")
                .foreignCountryPlate(false)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("cba-321")
                .foreignCountryPlate(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("abc-321")
                .foreignCountryPlate(false)
                .build();

        assertTrue(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }

    @Test
    void givenBlankString_whenValid_thenCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("")
                .foreignCountryPlate(false)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("")
                .foreignCountryPlate(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("")
                .foreignCountryPlate(false)
                .build();

        assertTrue(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }

    @Test
    void givenLicensePlateNumberSecondPattern_whenValid_thenCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("ab-12-12")
                .foreignCountryPlate(false)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("cb-21-21")
                .foreignCountryPlate(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("sd-43-54")
                .foreignCountryPlate(false)
                .build();

        assertTrue(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }

    @Test
    void givenLicensePlateNumberThirdPattern_whenValid_thenCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("p-12345")
                .foreignCountryPlate(false)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("p-54321")
                .foreignCountryPlate(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("p-12457")
                .foreignCountryPlate(false)
                .build();

        assertTrue(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }

    @Test
    void givenLicensePlateNumberFourthPattern_whenValid_thenCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("abc12345")
                .foreignCountryPlate(false)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("cba54321")
                .foreignCountryPlate(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("asd98765")
                .foreignCountryPlate(false)
                .build();

        assertTrue(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }

    @Test
    void givenLicensePlateNumber_whenForeignTrue_thenCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("abcds-321432")
                .foreignCountryPlate(true)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("abcds-321432")
                .foreignCountryPlate(true)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("abcds-321432")
                .foreignCountryPlate(true)
                .build();

        assertTrue(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertTrue(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }

    @Test
    void givenLicensePlateNumber_whenInvalid_thenUnCorrect(){

        when(validLicensePlate.licensePlate()).thenReturn("licensePlateNumber");
        when(validLicensePlate.foreignPlate()).thenReturn("foreignCountryPlate");

        LicensePlateNumberValidator licensePlateNumberValidator = new LicensePlateNumberValidator();
        licensePlateNumberValidator.initialize(validLicensePlate);

        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .licensePlateNumber("abcds-321432")
                .foreignCountryPlate(false)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .licensePlateNumber("abcds-321432")
                .foreignCountryPlate(false)
                .build();

        CarAndUserDTO carAndUserDTO = CarAndUserDTO.builder()
                .licensePlateNumber("abcds-321432")
                .foreignCountryPlate(false)
                .build();

        assertFalse(licensePlateNumberValidator.isValid(unauthorizedUserReservationDTO,constraintValidatorContext));
        assertFalse(licensePlateNumberValidator.isValid(carDTO,constraintValidatorContext));
        assertFalse(licensePlateNumberValidator.isValid(carAndUserDTO,constraintValidatorContext));
    }
}
