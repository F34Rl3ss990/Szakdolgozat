package com.car_service.egea1r.web.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.service.interfaces.CredentialService;
import com.car_service.egea1r.service.interfaces.PasswordResetTokenService;
import com.car_service.egea1r.web.data.payload.request.*;
import com.car_service.egea1r.web.data.payload.response.JwtResponse;
import com.car_service.egea1r.security.jwt.JwtUtilsImpl;
import com.car_service.egea1r.service.authentication.AuthCredentialDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class CredentialController {

    private final AuthenticationManager authenticationManager;
    private final CredentialService credentialService;
    private final JwtUtilsImpl jwtUtils;
    private final PasswordResetTokenService passwordresetTokenService;

    @Autowired
    public CredentialController(AuthenticationManager authenticationManager, CredentialService credentialService, JwtUtilsImpl jwtUtils, PasswordResetTokenService passwordresetTokenService) {
        this.authenticationManager = authenticationManager;
        this.credentialService = credentialService;
        this.jwtUtils = jwtUtils;
        this.passwordresetTokenService = passwordresetTokenService;
    }

    public static AuthCredentialDetailsImpl getAuthPrincipal() {
        return (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreAuthorize("hasRole('ROLE_BOSS')")
    @PostMapping(value = "/addAdmin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody AddAdminRequest addAdminRequest) {
        if (credentialService.credentialExistByEmail(addAdminRequest.getEmail()))
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        credentialService.addNewAdmin(addAdminRequest);
        return ResponseEntity.ok("Admin registered successfully!");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthCredentialDetailsImpl credentialDetails = setAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
        String role = credentialDetails.getAuthorities().toString();
        switch (role) {
            case "[ROLE_ADMIN]":
            case "[ROLE_BOSS]":
                return credentialService.authenticationChoose(credentialDetails.getUsername(), loginRequest);
            case "[ROLE_DISABLED]":
                return ResponseEntity.badRequest().body("Account is not verified");
            case "[ROLE_0]":
                return ResponseEntity.badRequest().body("Account is banned or disabled");
            default:
                return responseLoginInformation(credentialDetails);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<JwtResponse> verifyOTP(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest) {
        credentialService.verify(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
        AuthCredentialDetailsImpl credentialDetails = setAuthentication(verifyCodeRequest.getEmail(), verifyCodeRequest.getPassword());
        return responseLoginInformation(credentialDetails);
    }

    @GetMapping("/emailValid")
    public boolean emailValid(@RequestParam("email") String email) {
        return credentialService.credentialExistByEmail(email);
    }

    @GetMapping("/generateOtpNumber")
    @ResponseStatus(HttpStatus.OK)
    public void generateOtpNum() {
        credentialService.generateOtpNum(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public String registerUser(@RequestBody SignupRequest signupRequest) throws UnsupportedEncodingException, MessagingException {
        String path = "/api/auth/signup";
        return credentialService.createNewCredential(signupRequest.getEmail(), signupRequest.getPassword(), path);
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String verificationToken) throws UnsupportedEncodingException, MessagingException {
        return credentialService.confirmRegistration(verificationToken);
    }

    @GetMapping("/resetPassword")
    @ResponseStatus(HttpStatus.OK)
    public String resetPassword(
            @RequestParam("email") String email) throws UnsupportedEncodingException, MessagingException {
        Credential credential = credentialService.getByEmail(email);
        passwordresetTokenService.createPasswordResetTokenForCredentialAndSendIt(credential);
        return "Password reset token sent";
    }

    @GetMapping("/changePassword")
    public boolean validatePasswordResetToken(
            @RequestParam("token") String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String result = passwordresetTokenService.validatePasswordResetToken(passwordResetToken);
        if (result == null) {
            return true;
        } else {
            String email = passwordresetTokenService.getCredentialEmailByToken(passwordResetToken);
            Credential credential = credentialService.getByEmail(email);
            passwordresetTokenService.createPasswordResetTokenForCredentialAndSendIt(credential);
            return false;
        }
    }

    @PostMapping("/savePassword")
    public ResponseEntity<String> savePassword(
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        String result = passwordresetTokenService.validatePasswordResetToken(passwordResetRequest.getToken());
        if (result != null) {
            return ResponseEntity.badRequest().body("Invalid or expired password reset token");
        }
        Long credentialId = credentialService.getCredentialIdByToken(passwordResetRequest.getToken());
        if (credentialId != null) {
            credentialService.changePassword(credentialId, passwordResetRequest.getPassword());
            return ResponseEntity.ok("Password changed");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        Credential credential = credentialService.getPasswordAndIdByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        boolean validPassword = credentialService.checkIfValidOldPassword(credential.getPassword(), changePasswordRequest.getOldPassword());
        boolean verify = credentialService.verifyDataChange(changePasswordRequest.getOtpNum(), SecurityContextHolder.getContext().getAuthentication().getName());
        if (!validPassword) {
            return ResponseEntity.badRequest().body("Incorrect old password " + changePasswordRequest.getOldPassword());
        }
        if (!verify) {
            return ResponseEntity.badRequest().body("Invalid or expired otp number" + changePasswordRequest.getOtpNum());
        }
        credentialService.changePasswordAndBlockToken(request, credential.getCredentialId(), changePasswordRequest.getPassword());
        AuthCredentialDetailsImpl credentialDetails = setAuthentication(SecurityContextHolder.getContext().getAuthentication().getName(), changePasswordRequest.getPassword());
        return responseLoginInformation(credentialDetails);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        credentialService.saveBlockedToken(request);
    }

    private AuthCredentialDetailsImpl setAuthentication(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (AuthCredentialDetailsImpl) authentication.getPrincipal();
    }

    private ResponseEntity<JwtResponse> responseLoginInformation(AuthCredentialDetailsImpl credentialDetails) {
        String jwt = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(new JwtResponse(jwt,
                credentialDetails.getCredentialId(),
                credentialDetails.getUsername(),
                credentialDetails.getAuthorities().toString()));
    }
}

