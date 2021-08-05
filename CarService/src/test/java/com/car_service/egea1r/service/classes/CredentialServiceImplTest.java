package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.TokenBlock;
import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import com.car_service.egea1r.persistance.repository.interfaces.PasswordResetRepository;
import com.car_service.egea1r.persistance.repository.interfaces.TokenBlockRepository;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.security.EncrypterHelper;
import com.car_service.egea1r.security.jwt.JwtUtilId;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.OTPService;
import com.car_service.egea1r.service.interfaces.TotpManager;
import com.car_service.egea1r.service.interfaces.VerificationTokenService;
import com.car_service.egea1r.web.data.payload.request.AddAdminRequest;
import com.car_service.egea1r.web.data.payload.request.LoginRequest;
import com.car_service.egea1r.web.exception.BadRequestException;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(EncrypterHelper.class)
class CredentialServiceImplTest {

    @Mock
    CredentialRepository credentialRepository;
    @Mock
    PasswordResetRepository passwordResetRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenBlockRepository tokenBlockRepository;
    @Mock
    JwtUtilId jwtUtilId;
    @Mock
    TotpManager totpManager;
    @Mock
    OTPService otpService;
    @Mock
    EmailService emailService;
    @Mock
    VerificationTokenService verificationTokenService;
    @Mock
    UserRepository userRepository;
    @Mock
    EncrypterHelper encrypterHelper;

    @InjectMocks
    CredentialServiceImpl credentialService;

    Credential credential;
    final String email = "asd@asd.hu";

    @BeforeEach
    void setUp() {
        String password = "asdQWE123.";
        given(passwordEncoder.encode(password)).willReturn("$2a$12$NtM9KB8b9f3pMIC0G0Fabuar3EtjsdbiibVDXRaQJYZ6jAJ1RzcmW");
        credential = Credential.builder()
                .email("asd@asd.hu")
                .password(passwordEncoder.encode("asdQWE123."))
                .permission("ROLE_USER")
                .build();
    }

    @Test
    void credentialExistByEmail() {
        given(credentialRepository.existsByEmail(email)).willReturn(true);
        boolean exist = credentialService.credentialExistByEmail(email);
        assertTrue(exist);
    }

    @Test
    void credentialNotExistByEmail() {
        given(credentialRepository.existsByEmail(email)).willReturn(false);
        boolean exist = credentialService.credentialExistByEmail(email);
        assertFalse(exist);
    }

    @Test
    void getByEmail() {
        given(credentialRepository.getByEmail(email)).willReturn(Optional.of(credential));

        Credential credential2 = credentialService.getByEmail(email);

        assertEquals(credential, credential2);
        verify(credentialRepository, times(1)).getByEmail(email);
    }

    @Test
    void getByEmail2() {
        given(credentialRepository.getByEmail(email)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.getByEmail(email));

        String expectedMessage = "User not found by email: " + email;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).getByEmail(email);
    }


    @Test
    void getPasswordAndIdByEmail() {
        given(credentialRepository.getPasswordAndIdByEmail(email)).willReturn(Optional.of(credential));

        Credential credential2 = credentialService.getPasswordAndIdByEmail(email);

        assertEquals(credential, credential2);
        verify(credentialRepository, times(1)).getPasswordAndIdByEmail(email);
    }

    @Test
    void getPasswordAndIdByEmail2() {
        given(credentialRepository.getPasswordAndIdByEmail(email)).willReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.getPasswordAndIdByEmail(email));

        String expectedMessage = "User not found by email: " + email;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).getPasswordAndIdByEmail(email);
    }

    @Test
    void authenticationChooseEmail() {
        String authType = "email";
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("123456")
                .build();
        given(credentialRepository.getMultiFactorAuth(email)).willReturn(authType);
        given(otpService.generateOTP(email)).willReturn(123456);

        ResponseEntity<?> ret = credentialService.authenticationChoose(email, loginRequest);

        assertEquals(HttpStatus.OK, ret.getStatusCode());
        assertEquals(loginRequest, ret.getBody());
        verify(credentialRepository, times(1)).getMultiFactorAuth(email);
        verify(otpService, times(1)).generateOTP(email);
        verify(emailService, times(1)).sendSimpleMessage(email, "OTP - SpringBoot - CarService", "A bejelentkezéshez szükséges kódod: 123456");
    }

    @Test
    void authenticationChoosePhone() {
        String authType = "phone";
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("123456")
                .build();
        given(credentialRepository.getMultiFactorAuth(email)).willReturn(authType);

        ResponseEntity<?> ret = credentialService.authenticationChoose(email, loginRequest);

        assertEquals(HttpStatus.OK, ret.getStatusCode());
        assertEquals(loginRequest, ret.getBody());
        verify(credentialRepository, times(1)).getMultiFactorAuth(email);
    }

    @Test
    void authenticationChooseException() {
        String authType = "";
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("123456")
                .build();
        given(credentialRepository.getMultiFactorAuth(email)).willReturn(authType);

        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.authenticationChoose(email, loginRequest));

        String expectedMessage = "Multifactor authentication type does not exist!";
        String actualMessage = exception.getMessage();
        verify(credentialRepository, times(1)).getMultiFactorAuth(email);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void generateOtpNum() {
        given(otpService.generateOTP(email)).willReturn(123456);

        credentialService.generateOtpNum(email);

        verify(otpService, times(1)).generateOTP(email);
        verify(emailService, times(1)).sendSimpleMessage(email, "OTP - SpringBoot - CarService", "A bejelentkezéshez szükséges kódod: 123456");
    }

    @Test
    void addNewAdminPhone() {
        AddAdminRequest addAdminRequest = AddAdminRequest.builder()
                .email(email)
                .password("123456")
                .mfa("phone")
                .build();
        given(passwordEncoder.encode("123456")).willReturn("hashValue");
        given(totpManager.generateSecret()).willReturn("secret");

        Credential credential = credentialService.addNewAdmin(addAdminRequest);

        assertEquals(addAdminRequest.getEmail(), credential.getEmail());
        assertEquals("hashValue", credential.getPassword());
        assertEquals("ROLE_ADMIN", credential.getPermission());
        assertEquals("secret", credential.getSecret());
        verify(credentialRepository, times(1)).saveAdmin(credential);
        verify(totpManager, times(1)).generateSecret();
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void addNewAdminEmail() {
        AddAdminRequest addAdminRequest = AddAdminRequest.builder()
                .email(email)
                .password("123456")
                .mfa("email")
                .build();
        given(passwordEncoder.encode("123456")).willReturn("hashValue");

        Credential credential = credentialService.addNewAdmin(addAdminRequest);

        assertEquals(addAdminRequest.getEmail(), credential.getEmail());
        assertEquals("hashValue", credential.getPassword());
        assertEquals("ROLE_ADMIN", credential.getPermission());
        verify(credentialRepository, times(1)).saveAdmin(credential);
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void getCredentialIdByTokenCorrect() {
        String passwordResetToken = "123456asdfgh";
        long id = 2L;
        given(passwordResetRepository.getCredentialIdByPasswordResetToken(passwordResetToken)).willReturn(Optional.of(id));

        long credentialId = credentialService.getCredentialIdByToken(passwordResetToken);

        verify(passwordResetRepository, times(1)).getCredentialIdByPasswordResetToken(passwordResetToken);
        assertEquals(id, credentialId);
    }

    @Test
    void getCredentialIdByTokenException() {
        String passwordResetToken = "123456asdfgh";

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.getCredentialIdByToken(passwordResetToken));

        String expectedMessage = "Credential not found by token: " + passwordResetToken;
        String actualMessage = exception.getMessage();

        verify(passwordResetRepository, times(1)).getCredentialIdByPasswordResetToken(passwordResetToken);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createNewCredential() throws MessagingException, UnsupportedEncodingException {
        String password = "123456asdfgh";
        String path = "/api/auth/signup";
        long id = 5L;
        Credential credential = Credential.builder()
                .email(email)
                .password(password)
                .build();
        given(passwordEncoder.encode(password)).willReturn("hashValue");
        given(credentialRepository.saveUser(credential)).willReturn(id);

        String returnMessage = credentialService.createNewCredential(email, password, path);
        credential.setCredentialId(id);
        String expectedMessage = "User registered successfully!";
        assertTrue(returnMessage.contains(expectedMessage));
        verify(passwordEncoder, times(1)).encode(password);
        verify(credentialRepository, times(1)).saveUser(credential);
       // verify(verificationTokenService, times(1)).createVerificationToken(credential, "token");
       // verify(emailService, times(1)).sendVerificationToken(credential.getEmail(), "token");
    }

    @Test
    void changePassword() {
        long id = 5L;
        String password = "12345asdf";
        given(passwordEncoder.encode(password)).willReturn("hashValue");

        credentialService.changePassword(id, password);

        verify(passwordEncoder, times(1)).encode(password);
        verify(credentialRepository, times(1)).changePassword(5L, "hashValue");
    }

    @Test
    void checkIfValidOldPassword() {
        String oldPassword = "asd123";
        String password = "123asd";
        given(passwordEncoder.matches(oldPassword, password)).willReturn(false);

        boolean ret = credentialService.checkIfValidOldPassword(password, oldPassword);

        assertFalse(ret);
    }

    @Test
    void changePasswordAndBlockToken() {

    }

    @Test
    void saveBlockedToken() {
    }

    @Test
    void changeEmailAndBlockToken() {
    }

    @Test
    void findTokenBlock() {
    }

    @Test
    void getPermissionById() {
    }


    @Test
    void verifyDataChange() {
    }

    @Test
    void disableAccountByUser() {
    }

    @Test
    void disableAccountByAdmin() {
    }

    @Test
    void getAdminCredentialPage() {
    }

    @Test
    void confirmRegistration() {
    }
}
