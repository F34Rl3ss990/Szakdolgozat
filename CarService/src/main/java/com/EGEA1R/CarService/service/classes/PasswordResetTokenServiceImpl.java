package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.PasswordReset;
import com.EGEA1R.CarService.persistance.repository.interfaces.PasswordResetRepository;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.PasswordResetTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);

    private PasswordResetRepository passwordResetRepository;

    private EmailService emailService;

    @Autowired
    public void setPasswordResetTokenRepository(PasswordResetRepository passwordResetRepository){
        this.passwordResetRepository = passwordResetRepository;
    }

    @Autowired
    public void  setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    public String validatePasswordResetToken(String passwordResetToken) {
        final PasswordReset passToken = passwordResetRepository.getExpDateByResetToken(passwordResetToken)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("Password reset token not found by token: %s", passwordResetToken)));

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordReset passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordReset passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    @Transactional
    @Override
    public void createPasswordResetTokenForCredentialAndSendIt(Credential credential){
        try {
            String token = UUID.randomUUID().toString();
            String recipientAddress = credential.getEmail();
            PasswordReset tokenObj = PasswordReset.builder()
                    .token(token)
                    .expiryDate(calculateExpiryDate())
                    .build();
            passwordResetRepository.savePasswordResetToken(tokenObj, credential.getCredentialId());
            emailService.sendResetPasswordToken(recipientAddress, token);
        }catch (MessagingException e)
        {
            logger.error("Cannot send password reset token to this email: " + credential.getEmail() +". Error: " + e);
        }
    }
    private Date calculateExpiryDate() {
        final int EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
}