package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.TokenBlock;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.LoginRequest;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface CredentialService {

    Boolean credentialExistByEmail(String email);

    Credential getByEmail(String email);

    Long getCredentialIdByToken(String passwordResetToken);

    void createNewCredential(String email, String password, String path);

    boolean checkIfValidOldPassword(final String password, final String oldPassword);

    ResponseEntity<?> authenticationChoose(String email, LoginRequest loginRequest);

    Credential verify(String email, String code);

    Credential addNewAdmin(AddAdminRequest addAdminRequest);

    void disableAccountByUser(Long credentialId);

    void disableAccountByAdmin(Long userId);

    PagedListHolder<Credential> getAdminCredentialPage(int page, int size);

    void changePassword(final Long credentialId, final String password);

    Credential getPasswordAndIdByEmail(String email);

    void changePasswordAndBlockToken(HttpServletRequest request, Long credentialId, String password);

    String getPermissionById(Long verificationFkId);

    void saveBlockedToken(HttpServletRequest request);

}
