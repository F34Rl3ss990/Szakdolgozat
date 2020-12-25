package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.entity.Credential;
import com.EGEA1R.CarService.entity.User;
import com.EGEA1R.CarService.entity.VerificationToken;
import com.EGEA1R.CarService.repository.CredentialRepository;
import com.EGEA1R.CarService.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .build();
        verificationRepository.save(myToken);
    }

    @Override
    public void modifyPermissionOnVerificatedUser(Credential credential) {
        credentialRepository.save(credential);
    }
}
