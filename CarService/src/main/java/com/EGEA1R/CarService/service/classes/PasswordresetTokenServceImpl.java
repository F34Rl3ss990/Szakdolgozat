package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.entity.PasswordresetToken;
import com.EGEA1R.CarService.repository.PasswordresetTokenRepository;
import com.EGEA1R.CarService.service.interfaces.PasswordresetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PasswordresetTokenServceImpl implements PasswordresetTokenService {

    private PasswordresetTokenRepository passwordresetTokenRepository;

    @Autowired
    public void setPasswordresetTokenRepository(PasswordresetTokenRepository passwordresetTokenRepository){
        this.passwordresetTokenRepository = passwordresetTokenRepository;
    }

    public String validatePasswordResetToken(String token) {
        final PasswordresetToken passToken = passwordresetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordresetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordresetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
