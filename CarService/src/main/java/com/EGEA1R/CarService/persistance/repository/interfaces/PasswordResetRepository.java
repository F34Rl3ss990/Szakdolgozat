package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.PasswordReset;

import java.util.Optional;

public interface PasswordResetRepository {

    void savePasswordResetToken(PasswordReset passwordReset, Long credentialId);

    Optional<PasswordReset> getExpDateByResetToken(String passwordResetToken);

    Optional<Long> getCredentialIdByPasswordResetToken(String passwordResetToken);
}
