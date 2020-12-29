package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.entity.Credential;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CredentialService {

    Boolean credentialExistByEmail(String email);

    Optional<Credential> getByEmail(String email);

    void createPasswordResetTokenForCredential(Optional<Credential> credential, String token);

    Optional<Credential> getCredentialByToken(String token);

    Credential createNewCredential(String email, String password);

    void changeCredentialPassword(Credential credential1, String newPassword);

    void changeUserPassword(final Credential credential, final String password);

    boolean checkIfValidOldPassword(final Credential credential, final String oldPassword);
}
