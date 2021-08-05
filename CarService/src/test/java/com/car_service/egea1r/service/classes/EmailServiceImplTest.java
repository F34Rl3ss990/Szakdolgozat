package com.car_service.egea1r.service.classes;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceImplTest {

    EmailServiceImpl emailService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    SpringTemplateEngine springTemplateEngine;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        emailService = new EmailServiceImpl(javaMailSender, springTemplateEngine);
    }

    @Test
    void sendSimpleMessage() {
    }

    @Test
    void sendMessageWithAttachment() {
    }

    @Test
    void sendVerificationToken() {
    }

    @Test
    void resendVerificationToken() {
    }

    @Test
    void sendResetPasswordToken() {
    }

    @Test
    void sendReservedServiceInformation() {
    }
}
