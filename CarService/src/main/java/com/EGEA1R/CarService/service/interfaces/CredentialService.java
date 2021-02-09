package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

public interface CredentialService {

    Boolean credentialExistByEmail(String email);

    Credential getByEmail(String email);

    void createPasswordResetTokenForCredential(Credential credential, String token);

    Optional<Credential> getCredentialByToken(String token);

    Credential createNewCredential(String email, String password);

    void changeCredentialPassword(Credential credential1, String newPassword);

    void changeUserPassword(final Credential credential, final String password);

    boolean checkIfValidOldPassword(final Credential credential, final String oldPassword);

    String getMfa(String email);

    UsernamePasswordAuthenticationToken verify(String email, String code);
}
