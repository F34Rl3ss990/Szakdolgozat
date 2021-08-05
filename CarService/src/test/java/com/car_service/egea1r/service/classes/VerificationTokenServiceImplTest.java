package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import com.car_service.egea1r.persistance.repository.interfaces.VerificationRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class VerificationTokenServiceImplTest {

    VerificationTokenServiceImpl verificationTokenService;

    @Mock
    VerificationRepository verificationRepository;

    @Mock
    CredentialRepository credentialRepository;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        verificationTokenService = new VerificationTokenServiceImpl(verificationRepository, credentialRepository, emailService);
    }

    @Test
    void getFkAndExpDateByToken() {
    }

    @Test
    void createVerificationToken() {
    }

    @Test
    void modifyPermissionOnVerifiedUser() {
    }

    @Test
    void generateNewTokenAndSendItViaEmail() {
    }
}
