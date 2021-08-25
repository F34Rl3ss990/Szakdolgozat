package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import com.car_service.egea1r.persistance.repository.interfaces.VerificationRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceImplTest {

    @Mock
    VerificationRepository verificationRepository;

    @Mock
    CredentialRepository credentialRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    VerificationTokenServiceImpl verificationTokenService;

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
