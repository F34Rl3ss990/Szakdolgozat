package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Verification;

import java.util.Optional;

public interface VerificationRepository {

    Optional<Verification> getFkAndExpDateByToken(String verificationToken);

    Optional<Verification> findByToken(String verificationToken);

    void setNewTokenAndExpDateOnObj(Verification verification);

    String getCredentialEmailByVerificationId(Long verificationId);

    void saveVerificationToken(Verification verification, Long credentialId);
}
