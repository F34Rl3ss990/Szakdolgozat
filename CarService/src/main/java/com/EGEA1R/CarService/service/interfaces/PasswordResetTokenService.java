package com.EGEA1R.CarService.service.interfaces;


import com.EGEA1R.CarService.persistance.entity.Credential;

public interface PasswordResetTokenService {

    String validatePasswordResetToken(String token);

    void createPasswordResetTokenForCredentialAndSendIt(Credential credential);
}
