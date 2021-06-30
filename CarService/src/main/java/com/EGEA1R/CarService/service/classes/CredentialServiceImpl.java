package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.interfaces.UserRepository;
import com.EGEA1R.CarService.web.DTO.payload.request.LoginRequest;
import com.EGEA1R.CarService.web.exception.BadRequestException;
import com.EGEA1R.CarService.web.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.*;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.PasswordResetRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.TokenBlockRepository;
import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.security.jwt.JwtUtilId;
import com.EGEA1R.CarService.service.interfaces.*;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Service
public class CredentialServiceImpl implements CredentialService, JwtTokenCheckService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private CredentialRepository credentialRepository;

    private PasswordResetRepository passwordResetRepository;

    private PasswordEncoder passwordEncoder;

    private TokenBlockRepository tokenBlockRepository;

    private JwtUtilId jwtUtilId;

    private TotpManager totpManager;

    private OTPService otpService;

    private EmailService emailService;

    private VerificationTokenService verificationTokenService;

    private UserRepository userRepository;

    @Autowired
    public void setJwtUtilId(JwtUtilId jwtUtilId) {
        this.jwtUtilId = jwtUtilId;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Autowired
    public void setPasswordResetTokenRepository(PasswordResetRepository passwordResetRepository) {
        this.passwordResetRepository = passwordResetRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenBlockRepository(TokenBlockRepository tokenBlockRepository) {
        this.tokenBlockRepository = tokenBlockRepository;
    }

    @Autowired
    public void setTotpManager(TotpManager totpManager) {
        this.totpManager = totpManager;
    }

    @Autowired
    public void setOtpService(OTPService otpService) {
        this.otpService = otpService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setVerificationTokenService(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean credentialExistByEmail(String email) {
        return credentialRepository.existsByEmail(email);
    }

    @Override
    public Credential getByEmail(String email) {
        return credentialRepository
                .getByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with email: %s", email)));
    }

    @Override
    public Credential getPasswordAndIdByEmail(String email) {
        return credentialRepository
                .getPasswordAndIdByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with email: %s", email)));
    }

    @Override
    public ResponseEntity<?> authenticationChoose(String email, LoginRequest loginRequest) throws NullPointerException {
        String authType = credentialRepository.getMultiFactorAuth(email);
        if (authType.equals("email")) {
            int otp = otpService.generateOTP(email);
            String message = "A bejelentkezéshez szükséges kódod: " + otp;
            emailService.sendSimpleMessage(email, "OTP - SpringBoot - CarService", message);
            return ResponseEntity.ok(loginRequest);
        } else if (authType.equals("phone"))
            return ResponseEntity.ok(loginRequest);
        else {
            throw new BadRequestException("Multifactor authentication type does not exist!");
        }
    }

    @Override
    public void generateOtpNum(String email) {
        int otp = otpService.generateOTP(email);
        String message = "A bejelentkezéshez szükséges kódod: " + otp;
        emailService.sendSimpleMessage(email, "OTP - SpringBoot - CarService", message);
    }

    @Override
    public Credential addNewAdmin(AddAdminRequest addAdminRequest) {
        Credential credential = Credential.builder()
                .email(addAdminRequest.getEmail())
                .password(passwordEncoder.encode(addAdminRequest.getPassword()))
                .mfa(addAdminRequest.getMfa())
                .permission("ROLE_ADMIN")
                .build();
        if (addAdminRequest.getMfa().equals("phone")) {
            credential.setSecret(totpManager.generateSecret());
        }
        credentialRepository.saveAdmin(credential);
        return credential;
    }

    @Override
    public Long getCredentialIdByToken(String passwordResetToken) {
        return passwordResetRepository.getCredentialIdByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Credential not found by token: %s", passwordResetToken)));
    }

    @Transactional
    @Override
    public void createNewCredential(String email, String password, String path) throws UnsupportedEncodingException, MessagingException {
        Credential credential = Credential.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        Long id = credentialRepository.saveUser(credential);
        credential.setCredentialId(id);
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(credential, token);
        emailService.sendVerificationToken(credential.getEmail(), token);
    }

    @Override
    public void changePassword(final Long credentialId, final String password) {
        credentialRepository.changePassword(credentialId, passwordEncoder.encode(password));
    }

    @Override
    public boolean checkIfValidOldPassword(final String password, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, password);
    }

    @Transactional
    @Override
    public void changePasswordAndBlockToken(HttpServletRequest request, Long credentialId, String password) {
        changePassword(credentialId, password);
        saveBlockedToken(request);
    }

    @Override
    public void saveBlockedToken(HttpServletRequest request) {
        String jwt = parseJwt(request);
        jwt = EncrypterHelper.decrypt(jwt);
        String email = jwtUtilId.getEmailFromJwtToken(jwt);
        Long id = getByEmail(email).getCredentialId();
        TokenBlock tokenBlock = TokenBlock.builder()
                .userId(id)
                .jwtToken(jwt)
                .build();
        tokenBlockRepository.save(tokenBlock);
    }

    @Override
    @Transactional
    public void changeEmailAndBlockToken(HttpServletRequest request, Long credentialId, String email) {
        User user = userRepository.findUserByCredentialId(credentialId).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with this id: %s", credentialId)));
        credentialRepository.changeEmail(email, credentialId, user.getUserId());
        saveBlockedToken(request);
    }

    @Override
    public List<TokenBlock> findTokenBlock(String email) {
        return tokenBlockRepository.findByUserId(getByEmail(email).getCredentialId());
    }

    private String parseJwt(HttpServletRequest request) {
        final String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7, headerAuth.length());
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        return null;
    }

    @Override
    public String getPermissionById(Long verificationFkId) {
        return credentialRepository.getPermissionById(verificationFkId);
    }


    @Override
    public Credential verify(String email, String code) {
        Credential credential = credentialRepository
                .findByEmailForMFA(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("username %s", email)));
        String mfaType = credential.getMfa();
        Boolean verify = false;
        if (mfaType.equals("phone")) {
            verify = phoneVerify(code, credential);
        } else if (mfaType.equals("email")) {
            verify = emailVerify(code, email);
        }
        if (Boolean.TRUE.equals(verify)) {
            return credential;
        } else {
            throw new BadRequestException("Authentication was not successful");
        }
    }

    @Override
    public Boolean verifyDataChange(String code, String email) {
        Boolean verify = false;
        verify = emailVerify(code, email);
        return verify;
    }

    @Override
    public void disableAccountByUser(Long credentialId) {
        credentialRepository.disableAccountByUser(credentialId);
    }

    @Override
    public void disableAccountByAdmin(Long userId) {
        credentialRepository.disableUserAccountByAdmin(userId);
    }

    @Override
    public PagedListHolder<Credential> getAdminCredentialPage(int page, int size) {
        List<Credential> admins = credentialRepository.getAllAdmin();
        PagedListHolder<Credential> pageHolder = new PagedListHolder<>(admins);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }

    private Boolean phoneVerify(String code, Credential credential) {
        if (!totpManager.verifyCode(code, credential.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }
        return true;
    }

    private Boolean emailVerify(String code, String email) {
        int otpNum = Integer.parseInt(code);
        if (otpNum >= 0) {
            int serverOtp = otpService.getOtp(email);
            if (serverOtp > 0) {
                if (otpNum == serverOtp) {
                 //   otpService.clearOTP(email);
                    return true;
                } else {
                    throw new BadRequestException("Invalid OTP nubmer!");
                }
            } else {
                throw new BadRequestException("OTP number expired!");
            }
        } else {
            throw new BadRequestException("Invalid OTP number!");
        }
    }
}
