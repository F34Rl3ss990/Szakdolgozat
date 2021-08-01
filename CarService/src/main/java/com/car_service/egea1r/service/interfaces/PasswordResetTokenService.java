package com.car_service.egea1r.service.interfaces;


import com.car_service.egea1r.persistance.entity.Credential;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface PasswordResetTokenService {

    String validatePasswordResetToken(String token);

    void createPasswordResetTokenForCredentialAndSendIt(Credential credential) throws UnsupportedEncodingException, MessagingException;

    String getCredentialEmailByToken(String passwordResetToken);
}
