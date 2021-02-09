package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.VerificationToken;

public interface VerificationTokenService {

    Credential getCredential(String verificationToken);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken findByUser(Credential credential);

    void modifyPermissionOnVerificatedUser(Credential credential);

    void createVerificationToken(Credential credential, String token);

    VerificationToken generateNewVerificationToken(String existingToken);
}
