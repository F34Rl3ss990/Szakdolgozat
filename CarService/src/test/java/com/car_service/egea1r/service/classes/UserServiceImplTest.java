package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, mapStructObjectMapper, emailService);
    }

    @Test
    void saveUnauthorizedUser() {
    }

    @Test
    void modifyUser() {
    }

    @Test
    void getUserDetailsByCredentialId() {
    }

    @Test
    void getUserDetailsByCarId() {
    }

    @Test
    void getUserByCarId() {
    }

    @Test
    void modifyPhoneNumber() {
    }

    @Test
    void addCarAndUser() {
    }

    @Test
    void getAllUserPage() {
    }
}
