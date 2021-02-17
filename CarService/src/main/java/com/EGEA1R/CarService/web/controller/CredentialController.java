package com.EGEA1R.CarService.web.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.service.interfaces.*;
import com.EGEA1R.CarService.web.DTO.payload.request.ChangePasswordRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.PasswordResetRequest;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.VerificationToken;
import com.EGEA1R.CarService.events.OnRegistrationCompleteEvent;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.LoginRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.SignupRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.VerifyCodeRequest;
import com.EGEA1R.CarService.web.DTO.payload.response.JwtAuthenticationResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.JwtResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import com.EGEA1R.CarService.security.jwt.JwtUtilsImpl;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class CredentialController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private AuthenticationManager authenticationManager;

    private CredentialService credentialService;

    private JwtUtilsImpl jwtUtils;

    private ApplicationEventPublisher applicationEventPublisher;

    private VerificationTokenService verificationTokenService;

    private EmailService emailService;

    private PasswordresetTokenService passwordresetTokenService;

    private OTPService otpService;

    private JwtTokenCheckService jwtTokenCheckService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService){
        this.credentialService = credentialService;
    }

    @Autowired
    public void setJwtUtils(JwtUtilsImpl jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setVerificationTokenService(VerificationTokenService verificationTokenService){
        this.verificationTokenService = verificationTokenService;
    }

    @Autowired
    public void  setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Autowired
    public void setPasswordresetTokenService(PasswordresetTokenService passwordresetTokenService){
        this.passwordresetTokenService = passwordresetTokenService;
    }

    @Autowired
    public void setOtpService(OTPService otpService){
        this.otpService = otpService;
    }

    @Autowired
    public void setJwtTokenCheckService(JwtTokenCheckService jwtTokenCheckService){
        this.jwtTokenCheckService = jwtTokenCheckService;
    }

    @PreAuthorize("hasRole('ROLE_BOSS')")
    @PostMapping(value = "/addAdmin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AddAdminRequest addAdminRequest){
        if (credentialService.credentialExistByEmail(addAdminRequest.getEmail()))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        credentialService.addNewAdmin(addAdminRequest);
        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) authentication.getPrincipal();
        List<String> roles = credentialDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        for (String role : roles) {
            if (role.equals("ROLE_ADMIN")) {
                String email = authentication.getName();
                try {
                    if (credentialService.getMfa(email).equals("email")) {
                        int otp = otpService.generateOTP(email);
                        String messeage = "Your otp number is: " + otp;
                        emailService.sendSimpleMessage(email, "OTP -SpringBoot", messeage);
                        return ResponseEntity.ok(new JwtAuthenticationResponse("", "email"));
                    } else if (credentialService.getMfa(email).equals("phone")) {
                        return ResponseEntity.ok(new JwtAuthenticationResponse("", "phone"));
                    }
                }catch(NullPointerException n){
                    logger.error("Cannot set user authentication: {}", n);

            }

            }
        }
        String jwt = jwtUtils.generateJwtToken(authentication);
        jwt = EncrypterHelper.encrypt(jwt);

        return ResponseEntity.ok(new JwtResponse(jwt,
                credentialDetails.getCredentialId(),
                credentialDetails.getUsername(),
                roles));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                credentialService.verify(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return getResponseEntity(authentication);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        String path = "/api/auth/signup";
        if (credentialService.credentialExistByEmail(signupRequest.getEmail()))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        Credential credential = credentialService.createNewCredential(signupRequest.getEmail(), signupRequest.getPassword());
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(credential, path));
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {


        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }

        Credential credential = verificationToken.getCredential();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            try {
                resendRegistrationToken(token);
            } catch(MessagingException e){
                logger.error("Cannot send mail: {}", e);
            }
            return ResponseEntity.badRequest().body(new MessageResponse("redirect to bad user html, authentication token expired"));
        }

        credential.setPermission("ROLE_USER");
        verificationTokenService.modifyPermissionOnVerificatedUser(credential);
        return ResponseEntity.ok(new MessageResponse("Successfully verificated"));
    }


    private ResponseEntity<?> resendRegistrationToken(String existingToken) throws MessagingException {
        VerificationToken newToken = verificationTokenService.generateNewVerificationToken(existingToken);

        Credential credential = verificationTokenService.getCredential(newToken.getToken());
        String recipientAddress = credential.getEmail();
        emailService.resendVerificationToken(recipientAddress, newToken.getToken());
        return ResponseEntity.ok(new MessageResponse("Verification token resent"));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email) throws MessagingException {
        Credential credential = credentialService.getByEmail(email);
        if(credential!=null) {
        String token = UUID.randomUUID().toString();
        String recipientAddress = credential.getEmail();
        credentialService.createPasswordResetTokenForCredential(credential, token);
        emailService.sendResetPasswordToken(recipientAddress, token);
        return ResponseEntity.ok(new MessageResponse("Password reset token sent"));
        }else
            return ResponseEntity.badRequest().body(new MessageResponse("User cannot be found"));
    }

    @GetMapping("/changePassword")
    public ResponseEntity<?> showChangePasswordPage(
                                         @RequestParam("token") String token) {
        String result = passwordresetTokenService.validatePasswordResetToken(token);
        if(result != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid or expired password reset token"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Redirect to savePassword page"));
        }
    }

    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {

        String result = passwordresetTokenService.validatePasswordResetToken(passwordResetRequest.getToken());

        if(result != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid or expired password reset token"));
        }

        Optional<Credential> credential = credentialService.getCredentialByToken(passwordResetRequest.getToken());
        if(credential.isPresent()) {
            credentialService.changePassword(credential.get(), passwordResetRequest.getPassword());
            return ResponseEntity.ok(new MessageResponse("Password changed"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid password"));
        }
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        Credential credential = credentialService.getByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        if(credential!= null) {
            if (!credentialService.checkIfValidOldPassword(credential, changePasswordRequest.getOldPassword())) {
                // throw new InvalidOldPasswordException();
            }
            credentialService.changePassword(credential, changePasswordRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(SecurityContextHolder.getContext().getAuthentication().getName(), changePasswordRequest.getPassword()));
            jwtTokenCheckService.saveBlockedToken(request);
            return getResponseEntity(authentication);
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("User cannot be found"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        jwtTokenCheckService.saveBlockedToken(request);
        return ResponseEntity.ok(new MessageResponse("Logged out"));
    }

    @NotNull
    private ResponseEntity<?> getResponseEntity(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) authentication.getPrincipal();
        List<String> roles = credentialDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        String jwt = jwtUtils.generateJwtToken(authentication);
        jwt = EncrypterHelper.encrypt(jwt);

        return ResponseEntity.ok(new JwtResponse(jwt,
                credentialDetails.getCredentialId(),
                credentialDetails.getUsername(),
                roles));
    }
}

