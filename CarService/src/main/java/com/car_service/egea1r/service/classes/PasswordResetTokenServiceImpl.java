package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.PasswordReset;
import com.car_service.egea1r.persistance.repository.interfaces.PasswordResetRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.PasswordResetTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;

    @Autowired
    public PasswordResetTokenServiceImpl(PasswordResetRepository passwordResetRepository, EmailService emailService) {
        this.passwordResetRepository = passwordResetRepository;
        this.emailService = emailService;
    }

    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        final PasswordReset passToken = passwordResetRepository.getExpDateByResetToken(passwordResetToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Password reset token not found by token: %s", passwordResetToken)));

       if (isTokenExpired(passToken)) {
            return "expired";
        } else {
            return null;
        }

    }

    @Override
    public String getCredentialEmailByToken(String passwordResetToken) {
        return passwordResetRepository.getCredentialEmailByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Credential not found by token: %s", passwordResetToken)));
    }

    private boolean isTokenFound(PasswordReset passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordReset passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().compareTo(cal.getTime()) != 1;
    }

    @Transactional
    @Async
    @Override
    public void createPasswordResetTokenForCredentialAndSendIt(Credential credential) throws UnsupportedEncodingException, MessagingException {
        String token = UUID.randomUUID().toString();
        String recipientAddress = credential.getEmail();
        PasswordReset tokenObj = PasswordReset.builder()
                .token(token)
                .expiryDate(calculateExpiryDate())
                .build();
        passwordResetRepository.savePasswordResetToken(tokenObj, credential.getCredentialId());
        emailService.sendResetPasswordToken(recipientAddress, token);
    }

    private Date calculateExpiryDate() {
        final int EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
}
