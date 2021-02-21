package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.events.OnRegistrationCompleteEvent;
import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.InternalServerException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.*;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.PasswordResetRepository;
import com.EGEA1R.CarService.persistance.repository.TokenBlockRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.security.jwt.JwtUtilId;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.*;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

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

    private UserRepository userRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private EmailService emailService;

    @Autowired
    public void setJwtUtilId(JwtUtilId jwtUtilId){
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
    public void setTokenBlockRepository(TokenBlockRepository tokenBlockRepository){
        this.tokenBlockRepository = tokenBlockRepository;
    }

    @Autowired
    public void setTotpManager(TotpManager totpManager){
        this.totpManager = totpManager;
    }

    @Autowired
    public void setOtpService(OTPService otpService) {
        this.otpService = otpService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Override
    public Boolean credentialExistByEmail(String email) {
        return credentialRepository.existsByEmail(email);
    }

    @Override
    public Credential getByEmail(String email) {
        return  credentialRepository
                .getByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("User not found with email: %s", email)));
    }

    @Override
    public Credential getPasswordAndIdByEmail(String email) {
        return  credentialRepository
                .getPasswordAndIdByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("User not found with email: %s", email)));
    }

    @Override
    public String authenticationChoose(List<String> roles, String email){
        for(String role : roles){
            if(role.equals("ROLE_ADMIN") || role.equals("ROLE_BOSS")){
                try{
                    String authType = credentialRepository.getMultiFactorAuth(email);
                    if(authType.equals("email")){
                        int otp = otpService.generateOTP(email);
                        String message = "Your otp number is: " + otp;
                        emailService.sendSimpleMessage(email, "OTP - SpringBoot - CarService", message);
                        return "email";
                    } else if (authType.equals("phone"))
                        return "phone";
                }catch (NullPointerException n) {
                    logger.error("Cannot get authentication type from user: " + n);
                }
            }
        }
        return "";
    }

    @Override
    public Credential addNewAdmin(AddAdminRequest addAdminRequest) {
        Credential credential = Credential.builder()
                .email(addAdminRequest.getEmail())
                .password(passwordEncoder.encode(addAdminRequest.getPassword()))
                .mfa(addAdminRequest.getMfa())
                .permission("ROLE_ADMIN")
                .build();
        if(addAdminRequest.getMfa().equals("phone")){
            credential.setSecret(totpManager.generateSecret());
        }
        credentialRepository.saveAdmin(credential);
        return credential;
    }

    @Override
    public Long getCredentialIdByToken(String passwordResetToken) {
        return passwordResetRepository.getCredentialIdByPasswordResetToken(passwordResetToken)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("Credential not found by token: %s", passwordResetToken)));
    }

    @Transactional
    @Override
    public void createNewCredential(String email, String password, String path) {
        Credential credential = Credential.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        Long id = credentialRepository.saveUser(credential);
        credential.setCredentialId(id);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(credential, path));
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
    public void changePasswordAndBlockToken(HttpServletRequest request, Long credentialId, String password){
        changePassword(credentialId, password);
        saveBlockedToken(request);
    }

    @Override
    public void saveBlockedToken(HttpServletRequest request){
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
    public Optional<TokenBlock> findTokenBlock(String email){
        return tokenBlockRepository.findByUserId(getByEmail(email).getCredentialId());
    }

    private String parseJwt(HttpServletRequest request) {
        final String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")){
            return headerAuth.substring(7, headerAuth.length());
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        return null;
    }

    @Override
    public String getPermissionById(Long verificationFkId){
        return credentialRepository.getPermissionById(verificationFkId);
    }


    @Override
    public Credential verify(String email, String code) {
        Credential credential = credentialRepository
                .findByEmailForMFA(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("username %s", email)));
        String mfaType = credential.getMfa();
        Boolean verify = false;
        try {
            if (mfaType.equals("phone")) {
                verify = phoneVerify(code, credential);
            } else if (mfaType.equals("email")) {
                verify = emailVerify(code, email);
            }
        }catch(RuntimeException e){
            logger.error("Authentication was not successful" + e);
        }
        if(verify){
            return credential;
        } else{
            throw new BadRequestException("Authentication was not successful");
        }
    }
/*
    @Override
    public void disableAccountByUser(Long credentialId){
        Credential credential = getCredential(credentialId);
        credential.setPermission("ROLE_DISABLED");
        credentialRepository.save(credential);
    }

    @Override
    public void disableAccountByAdmin(Long userId){
        User user = getUser(userId);
        Credential credential = user.getCredential();
        credential.setPermission("ROLE_DISABLED");
        credentialRepository.save(credential);
    }

    public Page<Credential> getAdminCredentialPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "email"));
        Page<Credential> pageResult = credentialRepository.findAll(pageRequest);
        Predicate<Credential> contain = (Credential item) -> item.getPermission().equals("ROLE_ADMIN");
        List<Credential> credentials = pageResult
                .stream()
                .filter(contain)
                .collect(toList());
        return new PageImpl<>(credentials, pageRequest, pageResult.getTotalElements());
    }*/

    private User getUser(Long userId){
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with this id not found: %s", Long.toString(userId))));
    }

    private Credential getCredential(Long credentialId){
        return credentialRepository
                .findById(credentialId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Profile with this id not found: %s", Long.toString(credentialId))));
    }

    private Boolean phoneVerify(String code, Credential credential){
        if (!totpManager.verifyCode(code, credential.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }
        return true;
    }

    private Boolean emailVerify(String code, String email){
        int otpNum = Integer.parseInt(code);
        if(otpNum >= 0){
            int serverOtp = otpService.getOtp(email);
            if(serverOtp > 0){
                if(otpNum == serverOtp){
                    otpService.clearOTP(email);
                    return  true;
                }
                else {
                    return false;
                }
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
