package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

public interface CredentialService {

    Boolean credentialExistByEmail(String email);

    Credential getByEmail(String email);

    void createPasswordResetTokenForCredential(Credential credential, String token);

    Optional<Credential> getCredentialByToken(String token);

    Credential createNewCredential(String email, String password);

    void changePassword(final Credential credential, final String password);

    boolean checkIfValidOldPassword(final Credential credential, final String oldPassword);

    String getMfa(String email);

    UsernamePasswordAuthenticationToken verify(String email, String code);

    Credential addNewAdmin(AddAdminRequest addAdminRequest);

    void disableAccountByUser(Long credentialId);

    void disableAccountByAdmin(Long userId);

    Page<Credential> getAdminCredentialPage(int page, int size);
}
