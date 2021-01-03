package com.EGEA1R.CarService.events;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.VerificationTokenService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private VerificationTokenService verificationTokenService;

    private EmailService emailService;

    @Autowired
    public void  setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Autowired
    public void setVerificationTokenService(VerificationTokenService verificationTokenService){
        this.verificationTokenService = verificationTokenService;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        Credential credential = event.getCredential();
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(credential, token);
        emailService.sendVerificationToken(credential.getEmail(), token);
    }
}