package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.LoginRequest;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface CredentialService {

    Boolean credentialExistByEmail(String email);

    Credential getByEmail(String email);

    Long getCredentialIdByToken(String passwordResetToken);

    void createNewCredential(String email, String password, String path) throws UnsupportedEncodingException, MessagingException;

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

    void changeEmailAndBlockToken(HttpServletRequest request, Long credentialId, String email);

    void generateOtpNum(String email);

    Boolean verifyDataChange(String code, String email);
}
