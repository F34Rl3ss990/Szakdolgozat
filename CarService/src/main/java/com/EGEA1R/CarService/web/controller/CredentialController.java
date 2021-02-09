package com.EGEA1R.CarService.web.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.web.DTO.ChangePasswordDTO;
import com.EGEA1R.CarService.web.DTO.PasswordResetDTO;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.VerificationToken;
import com.EGEA1R.CarService.events.OnRegistrationCompleteEvent;
import com.EGEA1R.CarService.web.DTO.payload.request.LoginRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.SignupRequest;
import com.EGEA1R.CarService.web.DTO.payload.response.JwtResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import com.EGEA1R.CarService.security.jwt.JwtUtilsImpl;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.CredentialService;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.PasswordresetTokenService;
import com.EGEA1R.CarService.service.interfaces.VerificationTokenService;
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


    private AuthenticationManager authenticationManager;

    private CredentialService credentialService;

    private JwtUtilsImpl jwtUtils;

    private ApplicationEventPublisher applicationEventPublisher;

    private VerificationTokenService verificationTokenService;

    private EmailService emailService;

    private PasswordresetTokenService passwordresetTokenService;

    private EncrypterHelper encrypterHelper;

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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        jwt = EncrypterHelper.encrypt(jwt);
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) authentication.getPrincipal();
        List<String> roles = credentialDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

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
            return ResponseEntity.badRequest().body(new MessageResponse("redirect to bad user html, authentication token expired"));
        }

        credential.setPermission("ROLE_USER");
        verificationTokenService.modifyPermissionOnVerificatedUser(credential);
        return ResponseEntity.ok(new MessageResponse("Successfully verificated"));
    }

    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<?> resendRegistrationToken(
             @RequestParam("token") String existingToken) throws MessagingException {
        VerificationToken newToken = verificationTokenService.generateNewVerificationToken(existingToken);

        Credential credential = verificationTokenService.getCredential(newToken.getToken());
        String recipientAddress = credential.getEmail();
        emailService.resendVerificationToken(recipientAddress, newToken.getToken());
        return ResponseEntity.ok(new MessageResponse("Verification token resent"));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email) throws MessagingException {
        Optional<Credential> credential = credentialService.getByEmail(email);
        if (credential == null) {
         //   throw new UserNotFoundException();
        }
        if(credential.isPresent()) {
        String token = UUID.randomUUID().toString();
        String recipientAddress = credential.get().getEmail();
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
            @Valid @RequestBody PasswordResetDTO passwordResetDto) {

        String result = passwordresetTokenService.validatePasswordResetToken(passwordResetDto.getToken());

        if(result != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid or expired password reset token"));
        }

        Optional<Credential> credential = credentialService.getCredentialByToken(passwordResetDto.getToken());
        if(credential.isPresent()) {
            credentialService.changeCredentialPassword(credential.get(), passwordResetDto.getPassword());
            return ResponseEntity.ok(new MessageResponse("Password changed"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid password"));
        }
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        Optional<Credential> credential = credentialService.getByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        if(credential.isPresent()) {
            if (!credentialService.checkIfValidOldPassword(credential.get(), changePasswordDTO.getOldPassword())) {
                // throw new InvalidOldPasswordException();
            }
            credentialService.changeUserPassword(credential.get(), changePasswordDTO.getPassword());
            return ResponseEntity.ok(new MessageResponse("Password updated"));
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("User cannot be found"));
        }
    }
}

