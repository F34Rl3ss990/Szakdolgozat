package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.entity.TokenBlock;
import com.car_service.egea1r.persistance.entity.Verification;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.service.interfaces.*;
import com.car_service.egea1r.web.data.payload.request.LoginRequest;
import com.car_service.egea1r.web.exception.BadRequestException;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import com.car_service.egea1r.persistance.repository.interfaces.PasswordResetRepository;
import com.car_service.egea1r.persistance.repository.interfaces.TokenBlockRepository;
import com.car_service.egea1r.security.jwt.JwtUtilId;
import com.car_service.egea1r.web.data.payload.request.AddAdminRequest;
import com.car_service.egea1r.persistance.entity.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class CredentialServiceImpl implements CredentialService, JwtTokenCheckService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialServiceImpl.class);
    private final CredentialRepository credentialRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlockRepository tokenBlockRepository;
    private final JwtUtilId jwtUtilId;
    private final TotpManager totpManager;
    private final OTPService otpService;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;

    @Autowired
    public CredentialServiceImpl(CredentialRepository credentialRepository, PasswordResetRepository passwordResetRepository,
                                 PasswordEncoder passwordEncoder, TokenBlockRepository tokenBlockRepository, JwtUtilId jwtUtilId,
                                 TotpManager totpManager, OTPService otpService, EmailService emailService,
                                 VerificationTokenService verificationTokenService, UserRepository userRepository) {
        this.credentialRepository = credentialRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlockRepository = tokenBlockRepository;
        this.jwtUtilId = jwtUtilId;
        this.totpManager = totpManager;
        this.otpService = otpService;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean credentialExistByEmail(String email) {
        return credentialRepository.existsByEmail(email);
    }

    @Override
    public Credential getByEmail(String email) {
        return credentialRepository
                .getByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found by email: %s", email)));
    }

    @Override
    public Credential getPasswordAndIdByEmail(String email) {
        return credentialRepository
                .getPasswordAndIdByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found by email: %s", email)));
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
    public long getCredentialIdByToken(String passwordResetToken) {
        return passwordResetRepository.getCredentialIdByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Credential not found by token: %s", passwordResetToken)));
    }


    //email service thrown exceptions
    @Transactional
    @Override
    public String createNewCredential(String email, String password, String path) throws UnsupportedEncodingException, MessagingException {
        Credential credential = Credential.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        Long id = credentialRepository.saveUser(credential);
        credential.setCredentialId(id);
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(credential, token);
        emailService.sendVerificationToken(credential.getEmail(), token);
        return "User registered successfully!";
    }

    @Override
    public void changePassword(final long credentialId, final String password) {
        credentialRepository.changePassword(credentialId, passwordEncoder.encode(password));
    }

    @Override
    public boolean checkIfValidOldPassword(final String password, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, password);
    }

    @Transactional
    @Override
    public void changePasswordAndBlockToken(HttpServletRequest request, long credentialId, String password) {
        changePassword(credentialId, password);
        saveBlockedToken(request);
    }

    @Override
    public void saveBlockedToken(HttpServletRequest request) {
        String jwt = parseJwt(request);
        String email = jwtUtilId.getEmailFromJwtToken(jwt);
        long id = getByEmail(email).getCredentialId();
        TokenBlock tokenBlock = TokenBlock.builder()
                .userId(id)
                .jwtToken(jwt)
                .build();
        tokenBlockRepository.save(tokenBlock);
    }

    @Override
    @Transactional
    public void changeEmailAndBlockToken(HttpServletRequest request, long credentialId, String email) {
        User user = userRepository.findUserByCredentialId(credentialId).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with this id: %s", credentialId)));
        credentialRepository.changeEmail(email, credentialId, user.getUserId());
        saveBlockedToken(request);
    }

    @Override
    public List<TokenBlock> findTokenBlock(String email) {
        return tokenBlockRepository.findByUserId(getByEmail(email).getCredentialId());
    }

    private String parseJwt(@NotNull HttpServletRequest request) {
        final String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        return null;
    }

    @Override
    public String getPermissionById(long verificationFkId) {
        return credentialRepository.getPermissionById(verificationFkId);
    }


    @Override
    public Credential verify(String email, String code) {
        Credential credential = credentialRepository
                .findByEmailForMFA(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found by this email: %s", email)));
        String mfaType = credential.getMfa();
        boolean verify = false;
        if (mfaType.equals("phone")) {
            verify = phoneVerify(credential, code);
        } else if (mfaType.equals("email")) {
            verify = emailVerify(email, code);
        }
        if (verify) {
            return credential;
        } else {
            throw new BadRequestException("Authentication was not successful");
        }
    }

    @Override
    public boolean verifyDataChange(String email, String code) {
        return emailVerify(email, code);
    }

    @Override
    public void disableAccountByUser(long credentialId) {
        credentialRepository.disableAccountByUser(credentialId);
    }

    @Override
    public void disableAccountByAdmin(long userId) {
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

    @Override
    public ResponseEntity<String> confirmRegistration(String verificationToken) throws MessagingException, UnsupportedEncodingException {
        Verification verification = verificationTokenService.getFkAndExpDateByToken(verificationToken);
        String permission = getPermissionById(verification.getFkVerificationId());
        if (permission.equals("ROLE_DISABLED")) {
            return ResponseEntity.badRequest().body("This account is already verified or banned");
        }
        Calendar cal = Calendar.getInstance();
        if ((verification.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            verificationTokenService.generateNewTokenAndSendItViaEmail(verificationToken);
            return ResponseEntity.ok("Verification token resent");
        }
        verificationTokenService.modifyPermissionOnVerifiedUser(verification.getFkVerificationId());
        return ResponseEntity.ok("Successfully verified");
    }

    private boolean phoneVerify(Credential credential, String code) {
        if (!totpManager.verifyCode(code, credential.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }
        return true;
    }

    private boolean emailVerify(String email, String code) {
        int otpNum = Integer.parseInt(code);
        if (otpNum >= 0) {
            int serverOtp = otpService.getOtp(email);
            if (serverOtp > 0) {
                if (otpNum == serverOtp) {
                    otpService.clearOTP(email);
                    return true;
                } else {
                    throw new BadRequestException("Invalid OTP number!");
                }
            } else {
                throw new BadRequestException("OTP number expired!");
            }
        } else {
            throw new BadRequestException("Invalid OTP number!");
        }
    }
}
