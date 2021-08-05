package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.PasswordResetRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenServiceImplTest {

    PasswordResetTokenServiceImpl passwordResetTokenService;

    @Mock
    PasswordResetRepository passwordResetRepository;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        passwordResetTokenService = new PasswordResetTokenServiceImpl(passwordResetRepository, emailService);
    }

    @Test
    void validatePasswordResetToken() {
    }

    @Test
    void getCredentialEmailByToken() {
    }

    @Test
    void createPasswordResetTokenForCredentialAndSendIt() {
    }
}
