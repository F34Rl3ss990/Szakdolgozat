package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.Verification;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface VerificationTokenService {

    Verification getFkAndExpDateByToken(String verificationToken);

    void modifyPermissionOnVerifiedUser(long credentialId);

    void createVerificationToken(Credential credential, String token);

    void generateNewTokenAndSendItViaEmail(String existingToken) throws MessagingException, UnsupportedEncodingException;
}
