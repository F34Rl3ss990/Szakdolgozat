package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.*;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.support.PagedListHolder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @Mock
    EmailService emailService;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void saveUnauthorizedUser() throws MessagingException, UnsupportedEncodingException {
        String email = "billing@email.hu";
        String name = "Name Name";
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingEmail(email)
                .billingName(name)
                .email(email)
                .name(name)
                .brand("Audi")
                .type("A4")
                .mileage("20000")
                .reservedDate(new Date())
                .reservedServices("Kerékcsere")
                .build();
        BillingInformation billingInformation = BillingInformation.builder()
                .billingEmail(email)
                .billingName(name)
                .build();
        User user = User.builder()
                .name(name)
                .email(email)
                .build();
        List<CarMileage> carMileageList = new ArrayList<>();
        CarMileage carMileage = CarMileage.builder()
                .mileage("20000")
                .build();
        carMileageList.add(carMileage);
        Car car = Car.builder()
                .brand("Audi")
                .type("A4")
                .carMileages(carMileageList)
                .build();
        ServiceReservation serviceReservation = ServiceReservation.builder()
                .reservedDate(new Date())
                .reservedServices("Kerékcsere")
                .build();



        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoBillingInformation(unauthorizedUserReservationDTO)).willReturn(billingInformation);
        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoUser(unauthorizedUserReservationDTO)).willReturn(user);
        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoCar(unauthorizedUserReservationDTO)).willReturn(car);
        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoCarMileage(unauthorizedUserReservationDTO)).willReturn(carMileage);
        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoServiceReservation(unauthorizedUserReservationDTO)).willReturn(serviceReservation);

        userService.saveUnauthorizedUser(unauthorizedUserReservationDTO);

        user.setBillingInformation(billingInformation);
        verify(userRepository, times(1)).saveUnAuthorizedUser(user, car, serviceReservation);
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoBillingInformation(unauthorizedUserReservationDTO);
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoUser(unauthorizedUserReservationDTO);
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoCar(unauthorizedUserReservationDTO);
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoCarMileage(unauthorizedUserReservationDTO);
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoServiceReservation(unauthorizedUserReservationDTO);
        verify(emailService, times(1)).sendReservedServiceInformation(unauthorizedUserReservationDTO);
    }

    @Test
    void modifyUser() {
        given(userRepository.findUserByCredentialId()).willReturn();
        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoBillingInformation()).willReturn();
        given(mapStructObjectMapper.unauthorizedUserReservationDTOtoUser()).willReturn();

        userService.modifyUser();

        verify(userRepository, times(1)).modifyUserData();
        verify(userRepository, times(1)).findUserIdByCredentialId();
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoBillingInformation();
        verify(mapStructObjectMapper, times(1)).unauthorizedUserReservationDTOtoUser();

    }

    @Test
    void getUserDetailsByCredentialId() {
        given(userRepository.findUserByCredentialId()).willReturn();

        User retUser = userService.getUserDetailsByCredentialId();

        verify(userRepository, times(1)).findUserByCredentialId();
    }

    @Test
    void getUserDetailsByCredentialId_excp() {
        given(userRepository.findUserByCredentialId()).willReturn();

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserDetailsByCredentialId());
        String expectedMessage = "User with this id not found: " + "id here";

        verify(userRepository, times(1)).findUserByCredentialId();
    }

    @Test
    void getUserDetailsByCarId() {
        given(userRepository.findUserByCarId()).willReturn();

        UserDataDTO retUser = userService.getUserDetailsByCarId();

        verify(userRepository, times(1)).findUserByCarId();
    }

    @Test
    void getUserDetailsByCarId_excp() {
        given(userRepository.findUserByCarId()).willReturn();

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserDetailsByCarId());
        String expectedMessage = "User with this id not found: " + "id here";

        verify(userRepository, times(1)).findUserByCarId();
    }

    @Test
    void getUserByCarId() {
        given(userRepository.getUserByCarId()).willReturn();

        User retUser = userService.getUserByCarId();

        verify(userRepository, times(1)).getUserByCarId();
    }

    @Test
    void modifyPhoneNumber() {
        given(userRepository.findUserByCredentialId()).willReturn();

        userService.modifyPhoneNumber();

        verify(userRepository, times(1)).modifyPhoneNumber();
        verify(userRepository, times(1)).findUserIdByCredentialId();
    }

    @Test
    void addCarAndUser() {
        given(userRepository. findUserByCredentialId()).willReturn();

        userService.addCarAndUser();

        verify(userRepository, times(1)).findUserIdByCredentialId();
        verify(userRepository, times(1)).addCarAndUser();
    }

    @Test
    void addCarAndUser() {

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.addCarAndUser());
        String expectedMessage = "User with this credentialId not found: " + " id here";

        verify(userRepository, times(1)).findUserIdByCredentialId();
    }

    @Test
    void getAllUserPage() {
        given(userRepository.getAllUser()).willReturn();

        PagedListHolder<User> user = userService.getAllUserPage();

        verify(userRepository, times(1)).getAllUser();
    }
}
