package com.EGEA1R.CarService.web.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.service.interfaces.*;
import com.EGEA1R.CarService.web.DTO.payload.request.ChangePasswordRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.PasswordResetRequest;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.Verification;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    private VerificationTokenService verificationTokenService;

    private EmailService emailService;

    private PasswordResetTokenService passwordresetTokenService;

    private OTPService otpService;

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
    public void setVerificationTokenService(VerificationTokenService verificationTokenService){
        this.verificationTokenService = verificationTokenService;
    }

    @Autowired
    public void  setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Autowired
    public void setPasswordResetTokenService(PasswordResetTokenService passwordResetTokenService){
        this.passwordresetTokenService = passwordResetTokenService;
    }

    @Autowired
    public void setOtpService(OTPService otpService){
        this.otpService = otpService;
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
        String authType = credentialService.authenticationChoose(roles, authentication.getName());
        if(authType.equals("email"))
             return ResponseEntity.ok(loginRequest);
        else if (authType.equals("phone"))
             return ResponseEntity.ok(loginRequest);
        String jwt = jwtUtils.generateJwtToken(authentication);
        jwt = EncrypterHelper.encrypt(jwt);

        return ResponseEntity.ok(new JwtResponse(jwt,
                credentialDetails.getCredentialId(),
                credentialDetails.getUsername(),
                roles));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest){
        Credential credential =
                credentialService.verify(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(verifyCodeRequest.getEmail(), verifyCodeRequest.getPassword()));
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


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        String path = "/api/auth/signup";
        if (credentialService.credentialExistByEmail(signupRequest.getEmail()))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        credentialService.createNewCredential(signupRequest.getEmail(), signupRequest.getPassword(), path);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String verificationToken) {

        Verification verification = verificationTokenService.getFkAndExpDateByToken(verificationToken);
        if (verification == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
        String permission = credentialService.getPermissionById(verification.getFkVerificationId());
        if(!permission.equals("ROLE_DISABLED")){
            return ResponseEntity.badRequest().body(new MessageResponse("This account is already verified or banned"));
        }
        Calendar cal = Calendar.getInstance();
        if ((verification.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            try {
                verificationTokenService.generateNewTokenAndSendItViaEmail(verificationToken);
                return ResponseEntity.ok(new MessageResponse("Verification token resent"));
            } catch(MessagingException e){
                logger.error("Cannot send mail: {}", e);
            }
            return ResponseEntity.badRequest().body(new MessageResponse("redirect to bad user html, authentication token expired"));
        }

        verificationTokenService.modifyPermissionOnVerifiedUser(verification.getFkVerificationId());
        return ResponseEntity.ok(new MessageResponse("Successfully verified"));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email){
        Credential credential = credentialService.getByEmail(email);
        if(credential!=null) {
        passwordresetTokenService.createPasswordResetTokenForCredentialAndSendIt(credential);
        return ResponseEntity.ok(new MessageResponse("Password reset token sent"));
        }else
            return ResponseEntity.badRequest().body(new MessageResponse("User cannot be found"));
    }

    @GetMapping("/changePassword")
    public ResponseEntity<?> validatePasswordResetToken(
                                         @RequestParam("token") String passwordResetToken) {
        String result = passwordresetTokenService.validatePasswordResetToken(passwordResetToken);
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
        Long credentialId = credentialService.getCredentialIdByToken(passwordResetRequest.getToken());
        if(credentialId != null) {
            credentialService.changePassword(credentialId, passwordResetRequest.getPassword());
            return ResponseEntity.ok(new MessageResponse("Password changed"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid password"));
        }
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        Credential credential = credentialService.getPasswordAndIdByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        if(credential!= null) {
            if (!credentialService.checkIfValidOldPassword(credential.getPassword(), changePasswordRequest.getOldPassword())) {
                 throw new BadRequestException("Incorrect old password " + changePasswordRequest.getOldPassword());
            }
            credentialService.changePasswordAndBlockToken(request, credential.getCredentialId(), changePasswordRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(SecurityContextHolder.getContext().getAuthentication().getName(), changePasswordRequest.getPassword()));
            return getResponseEntity(authentication);
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("User cannot be found"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        credentialService.saveBlockedToken(request);
        return ResponseEntity.ok(new MessageResponse("Logged out"));
    }

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

