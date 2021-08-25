package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.PasswordReset;
import com.car_service.egea1r.persistance.repository.interfaces.PasswordResetRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceImplTest {

    @Mock
    PasswordResetRepository passwordResetRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    PasswordResetTokenServiceImpl passwordResetTokenService;

    final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    @Test
    void validatePasswordResetToken_whenValid_thenCorrect() {
        String passwordToken = "passToken";
        Date date = new Date();
        PasswordReset passwordReset = PasswordReset.builder()
                .expiryDate(new Date(date.getTime() + MILLIS_IN_A_DAY))
                .build();
        given(passwordResetRepository.getExpDateByResetToken(passwordToken)).willReturn(Optional.of(passwordReset));

        String actualMessage = passwordResetTokenService.validatePasswordResetToken(passwordToken);
        String expectedMessage = null;

        assertEquals(actualMessage, expectedMessage);
        verify(passwordResetRepository, times(1)).getExpDateByResetToken(passwordToken);
    }

    @Test
    void validatePasswordResetToken_whenExpired_thenInvalid() {
        String passwordToken = "passToken";
        Date date = new Date();
        PasswordReset passwordReset = PasswordReset.builder()
                .expiryDate(new Date(date.getTime() - MILLIS_IN_A_DAY))
                .build();
        given(passwordResetRepository.getExpDateByResetToken(passwordToken)).willReturn(Optional.of(passwordReset));

        String actualMessage = passwordResetTokenService.validatePasswordResetToken(passwordToken);
        String expectedMessage = "expired";

        assertTrue(actualMessage.contains(expectedMessage));
        verify(passwordResetRepository, times(1)).getExpDateByResetToken(passwordToken);
    }

    @Test
    void validatePasswordResetToken_whenNotFound_thenException() {
        String passwordToken = "passToken";
        Date date = new Date();

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> passwordResetTokenService.validatePasswordResetToken(passwordToken));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Password reset token not found by token: " + passwordToken;

        assertTrue(actualMessage.contains(expectedMessage));
        verify(passwordResetRepository, times(1)).getExpDateByResetToken(passwordToken);
    }

    @Test
    void getCredentialEmailByToken() {
        String passwordToken = "passwordToken";
        String email = "asd@asd.asd";
        given(passwordResetRepository.getCredentialEmailByPasswordResetToken(passwordToken)).willReturn(Optional.of(email));

        String actualMessage = passwordResetTokenService.getCredentialEmailByToken(passwordToken);

        assertNotNull(actualMessage);
        verify(passwordResetRepository, times(1)).getCredentialEmailByPasswordResetToken(passwordToken);
    }

    @Test
    void getCredentialEmailByToken2() {
        String passwordToken = "passwordToken";
        String email = "asd@asd.asd";

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> passwordResetTokenService.getCredentialEmailByToken(passwordToken));
        String expectedMessage = "Credential not found by token: " + passwordToken;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(passwordResetRepository, times(1)).getCredentialEmailByPasswordResetToken(passwordToken);
    }

    @Test
    void createPasswordResetTokenForCredentialAndSendIt_whenValid_thenCorrect() throws MessagingException, UnsupportedEncodingException {

        UUID defaultUuid = UUID.fromString("8d8b30e3-de52-4f1c-a71c-9905a8043dac");
        long credentialId = 4L;
        String email = "asd@asd.asd";
        final int EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        Credential credential = Credential.builder()
                .email(email)
                .credentialId(credentialId)
                .build();
        PasswordReset passwordReset = PasswordReset.builder()
                .token(defaultUuid.toString())
                .expiryDate(new Date(cal.getTime().getTime()))
                .build();
        try (MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class)) {
            mockedUuid.when(UUID::randomUUID).thenReturn(defaultUuid);

            passwordResetTokenService.createPasswordResetTokenForCredentialAndSendIt(credential);

            verify(passwordResetRepository, times(1)).savePasswordResetToken(passwordReset, credentialId);
            verify(emailService, times(1)).sendResetPasswordToken(email, defaultUuid.toString());

        }
    }
}
