package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.CarMileage;
import com.car_service.egea1r.persistance.entity.ServiceReservation;
import com.car_service.egea1r.persistance.repository.interfaces.ServiceReservationRepository;
import com.car_service.egea1r.service.interfaces.CarService;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.UserService;
import com.car_service.egea1r.web.data.DTO.ServiceReservationDTO;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import com.car_service.egea1r.web.data.DTO.UserCarsDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.support.PagedListHolder;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceReservationServiceImplTest {

    @Mock
    ServiceReservationRepository serviceReservationRepository;

    @Mock
    CarService carService;

    @Mock
    UserService userService;

    @Mock
    EmailService emailService;

    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @InjectMocks
    ServiceReservationServiceImpl serviceReservationService;

    @Test
    void saveService() throws MessagingException, UnsupportedEncodingException {
        long fkCarId = 4L;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        String reservedServices = "kerék csere, turbó csere";
        String comment = "nincs";
        ServiceReservationDTO serviceReservationDTO = ServiceReservationDTO.builder()
                .fkServiceReservationCarId(fkCarId)
                .reservedDate(new Date(date.getTime() + MILLIS_IN_A_DAY))
                .reservedServices(reservedServices)
                .comment(comment)
                .build();
        ServiceReservation serviceReservation = ServiceReservation.builder()
                .fkServiceReservationCarId(fkCarId)
                .reservedDate(new Date(date.getTime() + MILLIS_IN_A_DAY))
                .reservedServices(reservedServices)
                .comment(comment)
                .build();
        List<CarMileage> carMileageList = new ArrayList<>();
        CarMileage carMileage = new CarMileage();
        carMileage.setMileage("12345");
        carMileageList.add(carMileage);
        Car car = Car.builder()
                .brand("Audi")
                .type("A4")
                .engineType("1.9 PDTDI")
                .yearOfManufacture("2000")
                .engineNumber("1234567")
                .chassisNumber("12345679ö787dsa")
                .carMileages(carMileageList)
                .licensePlateNumber("abc-123")
                .build();
        UserDataDTO userDataDTO = UserDataDTO.builder()
                .name("Név Név")
                .email("asd@asd.asd")
                .phoneNumber("06201245789")
                .billingZipCode(1234)
                .billingTown("Miskolc")
                .billingOtherAddressType("1. emelet, 16. ajtó")
                .billingStreet("E/2")
                .billingName("Név Név")
                .billingPhoneNumber("06201245789")
                .billingEmail("asd@asd.asd")
                .billingTaxNumber("123542-1-22")
                .build();
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .name("Név Név")
                .email("asd@asd.asd")
                .phoneNumber("06201245789")
                .billingZipCode(1234)
                .billingTown("Miskolc")
                .billingOtherAddressType("1. emelet, 16. ajtó")
                .billingStreet("E/2")
                .billingName("Név Név")
                .billingPhoneNumber("06201245789")
                .billingEmail("asd@asd.asd")
                .billingTaxNumber("123542-1-22")
                .brand("Audi")
                .type("A4")
                .engineType("1.9 PDTDI")
                .yearOfManufacture("2000")
                .engineNumber("1234567")
                .chassisNumber("12345679ö787dsa")
                .mileage(carMileageList.get(0).getMileage())
                .licensePlateNumber("abc-123")
                .reservedDate(new Date(date.getTime() + MILLIS_IN_A_DAY))
                .reservedServices(reservedServices)
                .comment(comment)
                .build();

        given(mapStructObjectMapper.serviceReservationDTOtoServiceReservation(serviceReservationDTO)).willReturn(serviceReservation);
        given(carService.getCarById(serviceReservationDTO.getFkServiceReservationCarId())).willReturn(car);
        given(userService.getUserDetailsByCarId(serviceReservationDTO.getFkServiceReservationCarId())).willReturn(userDataDTO);

        serviceReservationService.saveService(serviceReservationDTO);

        verify(mapStructObjectMapper, times(1)).serviceReservationDTOtoServiceReservation(serviceReservationDTO);
        verify(serviceReservationRepository, times(1)).saveService(serviceReservation);
        verify(carService, times(1)).getCarById(serviceReservationDTO.getFkServiceReservationCarId());
        verify(userService, times(1)).getUserDetailsByCarId(serviceReservation.getFkServiceReservationCarId());
        verify(emailService, times(1)).sendReservedServiceInformation(unauthorizedUserReservationDTO);
    }

    @Test
    void getServicesByUserOrderByDate() {
        int page = 0;
        int size = 10;
        long userId = 3L;
        List<ServiceReservation> serviceReservations = new ArrayList<>();
        ServiceReservation serviceReservation = ServiceReservation.builder()
                .comment("asd")
                .serviceId(4L)
                .build();
        ServiceReservation serviceReservation2 = ServiceReservation.builder()
                .comment("asdasd")
                .serviceId(5L)
                .build();
        serviceReservations.add(serviceReservation);
        serviceReservations.add(serviceReservation2);

        given(serviceReservationRepository.getAllServiceByUser(userId)).willReturn(serviceReservations);

        PagedListHolder<ServiceReservation> pagedListHolder = serviceReservationService.getServicesByUserOrderByDate(page, size, userId);

        assertEquals(pagedListHolder.getSource().size(), serviceReservations.size());
        assertEquals(pagedListHolder.getSource(), serviceReservations);
        assertEquals(2, pagedListHolder.getNrOfElements());
        verify(serviceReservationRepository, times(1)).getAllServiceByUser(userId);
    }

    @Test
    void getServicesTodayOrderByDate() {
        int page = 0;
        int size = 10;
        LocalDate localDate = LocalDate.now();
        List<ServiceReservation> serviceReservations = new ArrayList<>();
        ServiceReservation serviceReservation = ServiceReservation.builder()
                .comment("asd")
                .serviceId(4L)
                .build();
        ServiceReservation serviceReservation2 = ServiceReservation.builder()
                .comment("asdasd")
                .serviceId(5L)
                .build();
        serviceReservations.add(serviceReservation);
        serviceReservations.add(serviceReservation2);

        given(serviceReservationRepository.getAllServiceByTodayDate(localDate)).willReturn(serviceReservations);

        PagedListHolder<ServiceReservation> pagedListHolder = serviceReservationService.getServicesTodayOrderByDate(page, size);

        assertEquals(pagedListHolder.getSource().size(), serviceReservations.size());
        assertEquals(pagedListHolder.getSource(), serviceReservations);
        assertEquals(2, pagedListHolder.getNrOfElements());
        verify(serviceReservationRepository, times(1)).getAllServiceByTodayDate(localDate);
    }

    @Test
    void getCarByCredentialId() throws ParseException {
        long credentialId = 5L;
        List<Car> carList = new ArrayList<>();
        CarMileage carMileage = CarMileage.builder()
                .mileage("12345")
                .dateOfSet(new Date(new Date().getTime() - 10000000))
                .build();
        CarMileage carMileage2 = CarMileage.builder()
                .mileage("123457")
                .dateOfSet(new Date())
                .build();
        CarMileage carMileage3 = CarMileage.builder()
                .mileage("123456")
                .dateOfSet(new Date(new Date().getTime() - 10000000))
                .build();
        List<CarMileage> carMileages = new ArrayList<>();
        carMileages.add(carMileage);
        carMileages.add(carMileage2);
        List<CarMileage> carMileages2 = new ArrayList<>();
        carMileages2.add(carMileage3);
        Car car = Car.builder()
                .carMileages(carMileages)
                .brand("Audi")
                .type("A4")
                .carId(3L)
                .build();
        Car car2 = Car.builder()
                .carMileages(carMileages2)
                .brand("Audi")
                .type("A5")
                .carId(4L)
                .build();
        carList.add(car);
        carList.add(car2);
        UserCarsDTO userCarsDTO = UserCarsDTO.builder()
                .brand("Audi")
                .type("A4")
                .build();
        UserCarsDTO userCarsDTO2 = UserCarsDTO.builder()
                .brand("Audi")
                .type("A5")
                .build();

        given(serviceReservationRepository.getAllCarByCredentialId(credentialId)).willReturn(carList);
        given(mapStructObjectMapper.carToUserCarsDTO(car)).willReturn(userCarsDTO);
        given(mapStructObjectMapper.carToUserCarsDTO(car2)).willReturn(userCarsDTO2);
        userCarsDTO.setMileage(carMileage2.getMileage());
        userCarsDTO.setDateOfSet(carMileage2.getDateOfSet());
        userCarsDTO2.setMileage(carMileage3.getMileage());
        userCarsDTO2.setDateOfSet(carMileage3.getDateOfSet());

        List<UserCarsDTO> userCarsDTOS = serviceReservationService.getCarByCredentialId(credentialId);

        assertEquals(2, userCarsDTOS.size());
        assertEquals(userCarsDTOS.get(0), userCarsDTO);
        assertEquals(userCarsDTOS.get(1), userCarsDTO2);
        verify(serviceReservationRepository, times(1)).getAllCarByCredentialId(credentialId);
        verify(mapStructObjectMapper, times(1)).carToUserCarsDTO(car);
        verify(mapStructObjectMapper, times(1)).carToUserCarsDTO(car2);
    }
}
