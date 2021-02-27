package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.web.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.Verification;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.VerificationRepository;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {


    private VerificationRepository verificationRepository;

    private CredentialRepository credentialRepository;

    private EmailService emailService;

    @Autowired
    public void  setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Autowired
    public void setVerificationRepository(VerificationRepository verificationRepository){
        this.verificationRepository = verificationRepository;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }

    @Override
    public Verification getFkAndExpDateByToken(String verificationToken) {
        return verificationRepository.getFkAndExpDateByToken(verificationToken)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("Credential not found by token: %s", verificationToken)));
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
        final int  EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public void modifyPermissionOnVerifiedUser(Long credentialId) {
        credentialRepository.setPermissionOnVerifiedUser(credentialId);
    }

    @Override
    public void generateNewTokenAndSendItViaEmail(String existingToken) throws MessagingException, UnsupportedEncodingException {
        Verification verification = verificationRepository.findByToken(existingToken)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("Verification not found by token: %s", existingToken)));
        String token = UUID.randomUUID().toString();
        verification.setToken(token);
        verification.setExpiryDate(calculateExpiryDate());
        verificationRepository.setNewTokenAndExpDateOnObj(verification);
        emailService.resendVerificationToken(getCredentialEmailById(verification.getVerificationId()), verification.getToken());
    }
}

