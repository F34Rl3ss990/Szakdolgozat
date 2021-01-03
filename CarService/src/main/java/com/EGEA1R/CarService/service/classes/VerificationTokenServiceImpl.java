package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.VerificationToken;
import com.EGEA1R.CarService.persistance.repository.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.VerificationRepository;
import com.EGEA1R.CarService.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private VerificationRepository verificationRepository;

    private CredentialRepository credentialRepository;

    @Autowired
    public void setVerificationRepository(VerificationRepository verificationRepository){
        this.verificationRepository = verificationRepository;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return verificationRepository.findByToken(VerificationToken);
    }

    @Override
    public Credential getCredential(String verificationToken) {
        Credential credential = verificationRepository.findByToken(verificationToken).getCredential();
        return credential;
    }

    @Override
    public VerificationToken findByUser(Credential credential){
        return verificationRepository.findByCredential(credential);
    }

    @Override
    public void createVerificationToken(Credential credential, String token) {
        VerificationToken myToken = VerificationToken.builder()
                .credential(credential)
                .token(token)
                .expiryDate(calculateExpiryDate())
                .build();
        verificationRepository.save(myToken);
    }

    private Date calculateExpiryDate() {
        final int  EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public void modifyPermissionOnVerificatedUser(Credential credential) {
        credentialRepository.save(credential);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingToken){
        VerificationToken verificationToken = verificationRepository.findByToken(existingToken);
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(calculateExpiryDate());
        verificationRepository.save(verificationToken);
        return verificationToken;
    }
}

