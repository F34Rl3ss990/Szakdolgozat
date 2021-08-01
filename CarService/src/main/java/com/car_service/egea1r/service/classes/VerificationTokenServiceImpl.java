package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.Verification;
import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import com.car_service.egea1r.persistance.repository.interfaces.VerificationRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {


    private final VerificationRepository verificationRepository;
    private final CredentialRepository credentialRepository;
    private final EmailService emailService;

    @Autowired
    public VerificationTokenServiceImpl(VerificationRepository verificationRepository, CredentialRepository credentialRepository, EmailService emailService) {
        this.verificationRepository = verificationRepository;
        this.credentialRepository = credentialRepository;
        this.emailService = emailService;
    }

    @Override
    public Verification getFkAndExpDateByToken(String verificationToken) {
        return verificationRepository.getFkAndExpDateByToken(verificationToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Credential not found by token: %s", verificationToken)));
    }


    private String getCredentialEmailById(Long id) {
        return verificationRepository.getCredentialEmailByVerificationId(id);
    }

    @Transactional
    @Override
    public void createVerificationToken(Credential credential, String token) {
        Verification myToken = Verification.builder()
                .token(token)
                .expiryDate(calculateExpiryDate())
                .build();
        verificationRepository.saveVerificationToken(myToken, credential.getCredentialId());
    }

    private Date calculateExpiryDate() {
        final int EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public void modifyPermissionOnVerifiedUser(long credentialId) {
        credentialRepository.setPermissionOnVerifiedUser(credentialId);
    }

    @Override
    public void generateNewTokenAndSendItViaEmail(String existingToken) throws MessagingException, UnsupportedEncodingException {
        Verification verification = verificationRepository.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Verification not found by token: %s", existingToken)));
        String token = UUID.randomUUID().toString();
        verification.setToken(token);
        verification.setExpiryDate(calculateExpiryDate());
        verificationRepository.setNewTokenAndExpDateOnObj(verification);
        emailService.resendVerificationToken(getCredentialEmailById(verification.getVerificationId()), verification.getToken());
    }
}

