package com.EGEA1R.CarService.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.EGEA1R.CarService.web.exception.BadRequestException;
import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.service.interfaces.*;
import com.EGEA1R.CarService.web.DTO.payload.request.ChangePasswordRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.PasswordResetRequest;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.Verification;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.LoginRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.SignupRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.VerifyCodeRequest;
import com.EGEA1R.CarService.web.DTO.payload.response.JwtResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import com.EGEA1R.CarService.security.jwt.JwtUtilsImpl;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private VerificationTokenService verificationTokenService;

    private PasswordResetTokenService passwordresetTokenService;

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
    public void setPasswordResetTokenService(PasswordResetTokenService passwordResetTokenService){
        this.passwordresetTokenService = passwordResetTokenService;
    }


    @PreAuthorize("hasRole('ROLE_BOSS')")
    @PostMapping(value = "/addAdmin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AddAdminRequest addAdminRequest){
        Boolean exist = credentialService.credentialExistByEmail(addAdminRequest.getEmail());
        if (Boolean.TRUE.equals(exist))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        credentialService.addNewAdmin(addAdminRequest);
        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthCredentialDetailsImpl credentialDetails = setAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
        String role = credentialDetails.getAuthorities().toString();
            if(role.equals("ROLE_ADMIN") || role.equals("ROLE_BOSS")){
                return credentialService.authenticationChoose(credentialDetails.getUsername(), loginRequest);

        }
        return responseLoginInformation(credentialDetails);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest){
        credentialService.verify(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
        AuthCredentialDetailsImpl credentialDetails = setAuthentication(verifyCodeRequest.getEmail(), verifyCodeRequest.getPassword());
        return responseLoginInformation(credentialDetails);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        String path = "/api/auth/signup";
        Boolean exist = credentialService.credentialExistByEmail(signupRequest.getEmail());
        if (Boolean.TRUE.equals(exist))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        credentialService.createNewCredential(signupRequest.getEmail(), signupRequest.getPassword(), path);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String verificationToken) throws UnsupportedEncodingException, MessagingException {

        Verification verification = verificationTokenService.getFkAndExpDateByToken(verificationToken);
        String permission = credentialService.getPermissionById(verification.getFkVerificationId());
        if(!permission.equals("ROLE_DISABLED")){
            return ResponseEntity.badRequest().body(new MessageResponse("This account is already verified or banned"));
        }
        Calendar cal = Calendar.getInstance();
        if ((verification.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                verificationTokenService.generateNewTokenAndSendItViaEmail(verificationToken);
                return ResponseEntity.ok(new MessageResponse("Verification token resent"));
        }
        verificationTokenService.modifyPermissionOnVerifiedUser(verification.getFkVerificationId());
        return ResponseEntity.ok(new MessageResponse("Successfully verified"));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email) throws UnsupportedEncodingException, MessagingException {
        Credential credential = credentialService.getByEmail(email);
        passwordresetTokenService.createPasswordResetTokenForCredentialAndSendIt(credential);
        return ResponseEntity.ok(new MessageResponse("Password reset token sent"));
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
            AuthCredentialDetailsImpl credentialDetails = setAuthentication(SecurityContextHolder.getContext().getAuthentication().getName(), changePasswordRequest.getPassword());
            return responseLoginInformation(credentialDetails);
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

    private AuthCredentialDetailsImpl setAuthentication(String email, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (AuthCredentialDetailsImpl) authentication.getPrincipal();
    }

    private ResponseEntity<?> responseLoginInformation(AuthCredentialDetailsImpl credentialDetails){
        String jwt = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());
        jwt = EncrypterHelper.encrypt(jwt);
        return ResponseEntity.ok(new JwtResponse(jwt,
                credentialDetails.getCredentialId(),
                credentialDetails.getUsername(),
                credentialDetails.getAuthorities().toString()));
    }
}

