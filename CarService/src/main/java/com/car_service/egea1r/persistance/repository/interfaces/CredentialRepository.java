package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.Credential;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository {

    Optional<Credential> findByEmail(String email);

    Optional<Credential> findByEmailForMFA(String email);

    void changeEmail(String email, long credentialId, long userId);

    Optional<Credential> findById (long credentialId);

    boolean existsByEmail(String email);

    void saveAdmin(Credential credential);

    long saveUser(Credential credential);

    void changePassword(final long credentialId, final String password);

    String getMultiFactorAuth(String email);

    Optional<Credential> getByEmail(String email);

    void setPermissionOnVerifiedUser(long credentialId);

    Optional<Credential> getPasswordAndIdByEmail(String email);

    String getPermissionById(long verificationFkId);

    void disableUserAccountByAdmin(long userid);

    void disableAccountByUser(long credentialId);

    List<Credential> getAllAdmin();

}
