package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.ServiceReservationRepository;
import com.car_service.egea1r.service.interfaces.CarService;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.UserService;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ServiceReservationServiceImplTest {

    ServiceReservationServiceImpl serviceReservationService;

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

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        serviceReservationService = new ServiceReservationServiceImpl(serviceReservationRepository, carService, userService,
                emailService, mapStructObjectMapper);
    }

    @Test
    void saveService() {
    }

    @Test
    void getServicesByUserOrderByDate() {
    }

    @Test
    void getServicesTodayOrderByDate() {
    }

    @Test
    void getCarByCredentialId() {
    }
}
