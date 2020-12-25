package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.entity.Credential;
import com.EGEA1R.CarService.entity.User;
import com.EGEA1R.CarService.entity.VerificationToken;
import org.springframework.stereotype.Service;

@Service
public interface VerificationTokenService {

    Credential getCredential(String verificationToken);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken findByUser(Credential credential);

    void modifyPermissionOnVerificatedUser(Credential credential);

    void createVerificationToken(Credential credential, String token);
}
