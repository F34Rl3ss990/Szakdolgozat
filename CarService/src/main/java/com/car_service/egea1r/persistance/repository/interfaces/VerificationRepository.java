package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.Verification;

import java.util.Optional;

public interface VerificationRepository {

    Optional<Verification> getFkAndExpDateByToken(String verificationToken);

    Optional<Verification> findByToken(String verificationToken);

    void setNewTokenAndExpDateOnObj(Verification verification);

    String getCredentialEmailByVerificationId(long verificationId);

    void saveVerificationToken(Verification verification, long credentialId);
}
