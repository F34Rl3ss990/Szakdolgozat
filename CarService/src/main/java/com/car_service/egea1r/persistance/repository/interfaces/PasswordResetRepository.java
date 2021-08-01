package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.PasswordReset;

import java.util.Optional;

public interface PasswordResetRepository {

    void savePasswordResetToken(PasswordReset passwordReset, long credentialId);

    Optional<PasswordReset> getExpDateByResetToken(String passwordResetToken);

    Optional<Long> getCredentialIdByPasswordResetToken(String passwordResetToken);

    Optional<String> getCredentialEmailByPasswordResetToken(String passwordResetToken);
}
