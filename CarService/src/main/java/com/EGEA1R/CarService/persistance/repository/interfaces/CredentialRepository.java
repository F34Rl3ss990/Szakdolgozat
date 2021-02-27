package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository {

    Optional<Credential> findByEmail(String email);

    Optional<Credential> findByEmailForMFA(String email);

    void changeEmail(String email, Long credentialId);

    Optional<Credential> findById (Long credentialId);

    Boolean existsByEmail(String email);

    void saveAdmin(Credential credential);

    Long saveUser(Credential credential);

    void changePassword(final Long credentialId, final String password);

    String getMultiFactorAuth(String email);

    Optional<Credential> getByEmail(String email);

    void setPermissionOnVerifiedUser(Long credentialId);

    Optional<Credential> getPasswordAndIdByEmail(String email);

    String getPermissionById(Long verificationFkId);

    void disableUserAccountByAdmin(Long userid);

    void disableAccountByUser(Long credentialId);

    List<Credential> getAllAdmin();

}
