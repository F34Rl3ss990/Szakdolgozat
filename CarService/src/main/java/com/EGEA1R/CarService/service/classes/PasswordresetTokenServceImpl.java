package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.PasswordResetToken;
import com.EGEA1R.CarService.persistance.repository.PasswordResetTokenRepository;
import com.EGEA1R.CarService.service.interfaces.PasswordresetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PasswordresetTokenServceImpl implements PasswordresetTokenService {

    private PasswordResetTokenRepository passwordresetTokenRepository;

    @Autowired
    public void setPasswordresetTokenRepository(PasswordResetTokenRepository passwordresetTokenRepository){
        this.passwordresetTokenRepository = passwordresetTokenRepository;
    }

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordresetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
