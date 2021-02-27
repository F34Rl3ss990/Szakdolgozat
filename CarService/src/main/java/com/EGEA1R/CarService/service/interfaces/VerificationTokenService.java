package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.Verification;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface VerificationTokenService {

    Verification getFkAndExpDateByToken(String verificationToken);

    void modifyPermissionOnVerifiedUser(Long credentialId);

    void createVerificationToken(Credential credential, String token);

    void generateNewTokenAndSendItViaEmail(String existingToken) throws MessagingException, UnsupportedEncodingException;
}
