package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.web.data.payload.request.AddAdminRequest;
import com.car_service.egea1r.web.data.payload.request.LoginRequest;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface CredentialService {

    boolean credentialExistByEmail(String email);

    Credential getByEmail(String email);

    long getCredentialIdByToken(String passwordResetToken);

    String createNewCredential(String email, String password, String path) throws UnsupportedEncodingException, MessagingException;

    boolean checkIfValidOldPassword(final String password, final String oldPassword);

    ResponseEntity<?> authenticationChoose(String email, LoginRequest loginRequest);

    Credential verify(String email, String code);

    Credential addNewAdmin(AddAdminRequest addAdminRequest);

    void disableAccountByUser(long credentialId);

    void disableAccountByAdmin(long userId);

    PagedListHolder<Credential> getAdminCredentialPage(int page, int size);

    void changePassword(final long credentialId, final String password);

    Credential getPasswordAndIdByEmail(String email);

    void changePasswordAndBlockToken(HttpServletRequest request, long credentialId, String password);

    String getPermissionById(long verificationFkId);

    void saveBlockedToken(HttpServletRequest request);

    void changeEmailAndBlockToken(HttpServletRequest request, long credentialId, String email);

    void generateOtpNum(String email);

    boolean verifyDataChange(String code, String email);

    ResponseEntity<String> confirmRegistration(String verificationToken) throws MessagingException, UnsupportedEncodingException;
}
