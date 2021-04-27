package com.EGEA1R.CarService.service.interfaces;


import com.EGEA1R.CarService.persistance.entity.Credential;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface PasswordResetTokenService {

    String validatePasswordResetToken(String token);

    void createPasswordResetTokenForCredentialAndSendIt(Credential credential) throws UnsupportedEncodingException, MessagingException;

    String getCredentialEmailByToken(String passwordResetToken);
}
