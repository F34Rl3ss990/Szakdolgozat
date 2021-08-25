package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.TokenBlock;
import com.car_service.egea1r.persistance.entity.User;
import com.car_service.egea1r.persistance.entity.Verification;
import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import com.car_service.egea1r.persistance.repository.interfaces.PasswordResetRepository;
import com.car_service.egea1r.persistance.repository.interfaces.TokenBlockRepository;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.security.jwt.JwtUtilId;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.OTPService;
import com.car_service.egea1r.service.interfaces.TotpManager;
import com.car_service.egea1r.service.interfaces.VerificationTokenService;
import com.car_service.egea1r.web.data.payload.request.AddAdminRequest;
import com.car_service.egea1r.web.data.payload.request.LoginRequest;
import com.car_service.egea1r.web.exception.BadRequestException;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @InjectMocks
    CredentialServiceImpl credentialService;

    Credential credential;
    final String email = "asd@asd.hu";
    final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

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
    void credentialExistByEmail_whenValid_thenCorrect() {
        given(credentialRepository.existsByEmail(email)).willReturn(true);
        boolean exist = credentialService.credentialExistByEmail(email);
        assertTrue(exist);
    }

    @Test
    void credentialNotExistByEmail_whenValid_thenCorrect() {
        given(credentialRepository.existsByEmail(email)).willReturn(false);
        boolean exist = credentialService.credentialExistByEmail(email);
        assertFalse(exist);
    }

    @Test
    void getByEmail_whenValid_thenCorrect() {
        given(credentialRepository.getByEmail(email)).willReturn(Optional.of(credential));

        Credential credential2 = credentialService.getByEmail(email);

        assertEquals(credential, credential2);
        verify(credentialRepository, times(1)).getByEmail(email);
    }

    @Test
    void getByEmail_whenInValid_thenException() {
        given(credentialRepository.getByEmail(email)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.getByEmail(email));

        String expectedMessage = "User not found by email: " + email;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).getByEmail(email);
    }


    @Test
    void getPasswordAndIdByEmail_whenValid_thenCorrect() {
        given(credentialRepository.getPasswordAndIdByEmail(email)).willReturn(Optional.of(credential));

        Credential credential2 = credentialService.getPasswordAndIdByEmail(email);

        assertEquals(credential, credential2);
        verify(credentialRepository, times(1)).getPasswordAndIdByEmail(email);
    }

    @Test
    void getPasswordAndIdByEmail_whenInValid_thenException() {
        given(credentialRepository.getPasswordAndIdByEmail(email)).willReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.getPasswordAndIdByEmail(email));

        String expectedMessage = "User not found by email: " + email;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).getPasswordAndIdByEmail(email);
    }

    @Test
    void authenticationChooseEmail_whenValid_thenCorrect() {
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
    void authenticationChoosePhone_whenValid_thenCorrect() {
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
    void authenticationChoose_whenInValid_thenException() {
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
    void generateOtpNum_whenValid_thenCorrect() {
        given(otpService.generateOTP(email)).willReturn(123456);

        credentialService.generateOtpNum(email);

        verify(otpService, times(1)).generateOTP(email);
        verify(emailService, times(1)).sendSimpleMessage(email, "OTP - SpringBoot - CarService", "A bejelentkezéshez szükséges kódod: 123456");
    }

    @Test
    void addNewAdminPhone_whenValid_thenCorrect() {
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
    void addNewAdminEmail_whenValid_thenCorrect() {
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
    void getCredentialIdByToken_whenValid_thenCorrect() {
        String passwordResetToken = "123456asdfgh";
        long id = 2L;
        given(passwordResetRepository.getCredentialIdByPasswordResetToken(passwordResetToken)).willReturn(Optional.of(id));

        long credentialId = credentialService.getCredentialIdByToken(passwordResetToken);

        verify(passwordResetRepository, times(1)).getCredentialIdByPasswordResetToken(passwordResetToken);
        assertEquals(id, credentialId);
    }

    @Test
    void getCredentialIdByToken_whenInValid_thenException() {
        String passwordResetToken = "123456asdfgh";

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.getCredentialIdByToken(passwordResetToken));

        String expectedMessage = "Credential not found by token: " + passwordResetToken;
        String actualMessage = exception.getMessage();

        verify(passwordResetRepository, times(1)).getCredentialIdByPasswordResetToken(passwordResetToken);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createNewCredential_whenValid_thenCorrect() throws MessagingException, UnsupportedEncodingException {
        String password = "123456asdfgh";
        String path = "/api/auth/signup";
        String expectedMessage = "User registered successfully!";
        UUID defaultUuid = UUID.fromString("8d8b30e3-de52-4f1c-a71c-9905a8043dac");
        long id = 5L;

        Credential credential = Credential.builder()
                .email(email)
                .password("hashValue")
                .build();
        given(passwordEncoder.encode(password)).willReturn("hashValue");
        given(credentialRepository.saveUser(credential)).willReturn(id);


        try (MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class)) {
            mockedUuid.when(UUID::randomUUID).thenReturn(defaultUuid);
            String returnMessage = credentialService.createNewCredential(email, password, path);
            assertTrue(returnMessage.contains(expectedMessage));
            verify(passwordEncoder, times(1)).encode(password);
            verify(emailService, times(1)).sendVerificationToken(credential.getEmail(), defaultUuid.toString());
            credential.setCredentialId(id);
            verify(credentialRepository, times(1)).saveUser(credential);
            verify(verificationTokenService, times(1)).createVerificationToken(credential, defaultUuid.toString());
        }
    }

    @Test
    void changePassword_whenValid_thenCorrect() {
        long id = 5L;
        String password = "12345asdf";
        given(passwordEncoder.encode(password)).willReturn("hashValue");

        credentialService.changePassword(id, password);

        verify(passwordEncoder, times(1)).encode(password);
        verify(credentialRepository, times(1)).changePassword(5L, "hashValue");
    }

    @Test
    void checkIfValidOldPassword_whenValid_thenCorrect() {
        String oldPassword = "asd123";
        String password = "123asd";
        given(passwordEncoder.matches(oldPassword, password)).willReturn(false);

        boolean ret = credentialService.checkIfValidOldPassword(password, oldPassword);

        assertFalse(ret);
    }

    @Test
    void changePasswordAndBlockToken_whenValid_thenCorrect() {
        String password = "123asd";
        String jwt = "Bearer asd1234321a";
        long id = 5L;
        long credentialId = 4L;
        Credential credential = Credential.builder()
                .credentialId(credentialId)
                .password(password)
                .build();
        TokenBlock tokenBlock = TokenBlock.builder()
                .userId(credentialId)
                .jwtToken(jwt.substring(7))
                .build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", jwt);
        given(passwordEncoder.encode(password)).willReturn("hashValue");
        given(jwtUtilId.getEmailFromJwtToken(jwt.substring(7))).willReturn(email);
        given(credentialRepository.getByEmail(email)).willReturn(Optional.of(credential));

        credentialService.changePasswordAndBlockToken(request, id, password);

        verify(credentialRepository, times(1)).changePassword(id, "hashValue");
        verify(credentialRepository, times(1)).getByEmail(email);
        verify(tokenBlockRepository, times(1)).save(tokenBlock);
        verify(jwtUtilId, times(1)).getEmailFromJwtToken(jwt.substring(7));
    }

    @Test
    void saveBlockedToken_whenValid_thenCorrect() {
        String jwt = "Bearer asd1234321a";
        long credentialId = 4L;
        Credential credential = Credential.builder()
                .credentialId(credentialId)
                .build();
        TokenBlock tokenBlock = TokenBlock.builder()
                .userId(credentialId)
                .jwtToken(jwt.substring(7))
                .build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", jwt);
        given(jwtUtilId.getEmailFromJwtToken(jwt.substring(7))).willReturn(email);
        given(credentialRepository.getByEmail(email)).willReturn(Optional.of(credential));

        credentialService.saveBlockedToken(request);

        verify(credentialRepository, times(1)).getByEmail(email);
        verify(tokenBlockRepository, times(1)).save(tokenBlock);
        verify(jwtUtilId, times(1)).getEmailFromJwtToken(jwt.substring(7));
    }

    @Test
    void changeEmailAndBlockToken_whenValid_thenCorrect() {
        String jwt = "Bearer asd1234321a";
        long credentialId = 4L;
        long userId = 5L;
        Credential credential = Credential.builder()
                .credentialId(credentialId)
                .build();
        User user = User.builder()
                .userId(userId)
                .build();
        TokenBlock tokenBlock = TokenBlock.builder()
                .userId(credentialId)
                .jwtToken(jwt.substring(7))
                .build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", jwt);
        given(jwtUtilId.getEmailFromJwtToken(jwt.substring(7))).willReturn(email);
        given(credentialRepository.getByEmail(email)).willReturn(Optional.of(credential));
        given(userRepository.findUserByCredentialId(credentialId)).willReturn(Optional.of(user));

        credentialService.changeEmailAndBlockToken(request, credentialId, email);

        verify(userRepository, times(1)).findUserByCredentialId(credentialId);
        verify(credentialRepository, times(1)).changeEmail(email, credentialId, user.getUserId());
        verify(credentialRepository, times(1)).getByEmail(email);
        verify(tokenBlockRepository, times(1)).save(tokenBlock);
        verify(jwtUtilId, times(1)).getEmailFromJwtToken(jwt.substring(7));
    }

    @Test
    void changeEmailAndBlockToken_whenInValid_thenUncorrect() {
        String jwt = "Bearer asd1234321a";
        long credentialId = 4L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", jwt);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.changeEmailAndBlockToken(request, credentialId, email));
        String expectedMessage = "User not found with this id: " + credentialId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).findUserByCredentialId(credentialId);
    }

    @Test
    void findTokenBlock_whenValid_thenCorrect() {
        long credentialId = 4L;
        Credential credential = Credential.builder()
                .credentialId(credentialId)
                .build();
        TokenBlock tokenBlock = TokenBlock.builder()
                .jwtToken("randomToken")
                .userId(3L)
                .tokenBlockId("randomUUid")
                .build();
        TokenBlock tokenBlock2 = TokenBlock.builder()
                .jwtToken("randomToken2")
                .userId(5L)
                .tokenBlockId("randomUUid2.0")
                .build();
        List<TokenBlock> tokenBlocks = new ArrayList<>();
        tokenBlocks.add(tokenBlock);
        tokenBlocks.add(tokenBlock2);
        given(credentialRepository.getByEmail(email)).willReturn(Optional.of(credential));
        given(tokenBlockRepository.findByUserId(credentialId)).willReturn(tokenBlocks);

        List<TokenBlock> tokenBlockList = credentialService.findTokenBlock(email);

        assertEquals(tokenBlockList.size(), tokenBlocks.size());
        assertTrue(tokenBlockList.contains(tokenBlock));
        assertTrue(tokenBlockList.contains(tokenBlock2));
        verify(tokenBlockRepository, times(1)).findByUserId(credentialId);
        verify(credentialRepository, times(1)).getByEmail(email);
    }

    @Test
    void findTokenBlock_whenInvalid_thenException() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.findTokenBlock(email));

        String expectedMessage = "User not found by email: " + email;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).getByEmail(email);
    }

    @Test
    void getPermissionById_whenValid_thenCorrect() {
        long verificationId = 4L;
        String role = "[ROLE_USER]";
        given(credentialRepository.getPermissionById(verificationId)).willReturn(role);

        String permission = credentialService.getPermissionById(verificationId);
        assertEquals(permission, role);
        verify(credentialRepository, times(1)).getPermissionById(verificationId);
    }

    @Test
    void verify_whenInvalid_thenException() {
        String code = "1234";
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> credentialService.verify(email, code));
        String expectedMessage = "User not found by this email: " + email;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
    }

    @Test
    void verify_whenNullString_thenException() {
        String code = "1234";
        Credential credential = Credential.builder()
                .mfa("")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));

        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verify(email, code));
        String expectedMessage = "Authentication was not successful";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
    }

    @Test
    void verify_whenPhoneAndInvalid_thenException() {
        String code = "1234";
        Credential credential = Credential.builder()
                .mfa("phone")
                .secret("secret")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));
        given(totpManager.verifyCode(code, credential.getSecret())).willReturn(false);

        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verify(email, code));
        String expectedMessage = "Code is incorrect";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
        verify(totpManager, times(1)).verifyCode(code, credential.getSecret());
    }

    @Test
    void verify_whenPhoneAndValid_thenCorrect() {
        String code = "1234";
        Credential credential = Credential.builder()
                .mfa("phone")
                .secret("secret")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));
        given(totpManager.verifyCode(code, credential.getSecret())).willReturn(true);

        Credential retCredential = credentialService.verify(email, code);

        assertEquals(retCredential, credential);
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
        verify(totpManager, times(1)).verifyCode(code, credential.getSecret());
    }

    @Test
    void verify_whenEmailAndInvalidFirst_thenException() {
        String code = "-2";
        Credential credential = Credential.builder()
                .mfa("email")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));
        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verify(email, code));
        String expectedMessage = "Invalid OTP number!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
    }

    @Test
    void verify_whenEmailAndInvalidSecond_thenException() {
        String code = "1234";
        Credential credential = Credential.builder()
                .mfa("email")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));
        given(otpService.getOtp(email)).willReturn(-1);
        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verify(email, code));
        String expectedMessage = "OTP number expired!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
        verify(otpService, times(1)).getOtp(email);
    }

    @Test
    void verify_whenEmailAndInvalidThird_thenException() {
        String code = "1234";
        Credential credential = Credential.builder()
                .mfa("email")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));
        given(otpService.getOtp(email)).willReturn(12345);

        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verify(email, code));
        String expectedMessage = "Invalid OTP number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
        verify(otpService, times(1)).getOtp(email);
    }

    @Test
    void verify_whenEmailAndValid_thenCorrect() {
        String code = "1234";
        Credential credential = Credential.builder()
                .mfa("email")
                .build();
        given(credentialRepository.findByEmailForMFA(email)).willReturn(Optional.of(credential));
        given(otpService.getOtp(email)).willReturn(1234);

        Credential retCredential = credentialService.verify(email, code);

        assertEquals(retCredential, credential);
        verify(otpService, times(1)).clearOTP(email);
        verify(credentialRepository, times(1)).findByEmailForMFA(email);
        verify(otpService, times(1)).getOtp(email);
    }

    @Test
    void verifyDataChange_whenInvalidFirst_thenException() {
        String code = "-2";
        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verifyDataChange(email, code));
        String expectedMessage = "Invalid OTP number!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void verifyDataChange_whenInvalidSecond_thenException() {
        String code = "1234";
        given(otpService.getOtp(email)).willReturn(-1);
        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verifyDataChange(email, code));
        String expectedMessage = "OTP number expired!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(otpService, times(1)).getOtp(email);
    }

    @Test
    void verifyDataChange_whenInvalidThird_thenException() {
        String code = "1234";
        given(otpService.getOtp(email)).willReturn(12345);

        Exception exception = assertThrows(BadRequestException.class, () -> credentialService.verifyDataChange(email, code));
        String expectedMessage = "Invalid OTP number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(otpService, times(1)).getOtp(email);
    }

    @Test
    void verifyDataChange_whenValid_thenCorrect() {
        String code = "1234";
        given(otpService.getOtp(email)).willReturn(1234);

        boolean retVal = credentialService.verifyDataChange(email, code);

        assertTrue(retVal);
        verify(otpService, times(1)).clearOTP(email);
        verify(otpService, times(1)).getOtp(email);
    }


    @Test
    void disableAccountByUser_whenValid_thenCorrect() {
        long credentialId = 4L;
        credentialService.disableAccountByUser(credentialId);
        verify(credentialRepository, times(1)).disableAccountByUser(credentialId);
    }

    @Test
    void disableAccountByAdmin_whenValid_thenCorrect() {
        long credentialId = 4L;
        credentialService.disableAccountByAdmin(credentialId);
        verify(credentialRepository, times(1)).disableUserAccountByAdmin(credentialId);
    }

    @Test
    void getAdminCredentialPage_whenValid_thenCorrect() {
        int page = 0;
        int size = 10;
        Credential credential = Credential.builder()
                .credentialId(1L)
                .email("asd@asd.hu")
                .password("1234567")
                .build();
        Credential credential2 = Credential.builder()
                .credentialId(2L)
                .email("asdasd@asd.hu")
                .password("1234567abcdefg")
                .build();
        List<Credential> credentials = new ArrayList<>();
        credentials.add(credential);
        credentials.add(credential2);
        given(credentialRepository.getAllAdmin()).willReturn(credentials);

        PagedListHolder<Credential> admins = credentialService.getAdminCredentialPage(page, size);

        assertEquals(admins.getSource().size(), credentials.size());
        assertEquals(admins.getSource(), credentials);
        assertEquals(2, admins.getNrOfElements());
        verify(credentialRepository, times(1)).getAllAdmin();
    }

    @Test
    void confirmRegistration_whenDisabled_thenBadRequest() throws MessagingException, UnsupportedEncodingException {
        String verificationToken = "asd123";
        long verificationId = 4L;
        String role = "ROLE_DISABLED";
        Verification verification = Verification.builder()
                .expiryDate(new Date())
                .fkVerificationId(4L)
                .build();

        given(verificationTokenService.getFkAndExpDateByToken(verificationToken)).willReturn(verification);
        given(credentialRepository.getPermissionById(verificationId)).willReturn(role);

        ResponseEntity<String> actualMessage = credentialService.confirmRegistration(verificationToken);
        String expectedMessage = "This account is already verified or banned";

        assertTrue(actualMessage.getBody().contains(expectedMessage));
        assertEquals(HttpStatus.BAD_REQUEST, actualMessage.getStatusCode());
        verify(credentialRepository, times(1)).getPermissionById(verificationId);
        verify(verificationTokenService, times(1)).getFkAndExpDateByToken(verificationToken);
    }
    @Test
    void confirmRegistration_whenExpiredToken_thenResent() throws MessagingException, UnsupportedEncodingException {
        String verificationToken = "asd123";
        long verificationId = 4L;
        Date date = new Date();
        String role = "ROLE_USER";
        Verification verification = Verification.builder()
                .expiryDate(new Date(date.getTime() - MILLIS_IN_A_DAY))
                .fkVerificationId(4L)
                .build();

        given(verificationTokenService.getFkAndExpDateByToken(verificationToken)).willReturn(verification);
        given(credentialRepository.getPermissionById(verificationId)).willReturn(role);

        ResponseEntity<String> actualMessage = credentialService.confirmRegistration(verificationToken);
        String expectedMessage = "Verification token resent";
        assertTrue(actualMessage.getBody().contains(expectedMessage));
        assertEquals(HttpStatus.OK, actualMessage.getStatusCode());
        verify(verificationTokenService, times(1)).generateNewTokenAndSendItViaEmail(verificationToken);
        verify(credentialRepository, times(1)).getPermissionById(verificationId);
        verify(verificationTokenService, times(1)).getFkAndExpDateByToken(verificationToken);
    }

    @Test
    void confirmRegistration_whenTokenNotExpired_thenSuccess() throws MessagingException, UnsupportedEncodingException {
        String verificationToken = "asd123";
        long verificationId = 4L;
        Date date = new Date();
        String role = "ROLE_USER";
        Verification verification = Verification.builder()
                .expiryDate(new Date(date.getTime() + MILLIS_IN_A_DAY))
                .fkVerificationId(4L)
                .build();

        given(verificationTokenService.getFkAndExpDateByToken(verificationToken)).willReturn(verification);
        given(credentialRepository.getPermissionById(verificationId)).willReturn(role);

        ResponseEntity<String> actualMessage = credentialService.confirmRegistration(verificationToken);
        String expectedMessage = "Successfully verified";
        assertTrue(actualMessage.getBody().contains(expectedMessage));
        assertEquals(HttpStatus.OK, actualMessage.getStatusCode());
        verify(verificationTokenService, times(1)).modifyPermissionOnVerifiedUser(verification.getFkVerificationId());
        verify(credentialRepository, times(1)).getPermissionById(verificationId);
        verify(verificationTokenService, times(1)).getFkAndExpDateByToken(verificationToken);
    }


}
