package com.EGEA1R.CarService.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.EGEA1R.CarService.controller.DTO.PasswordDTO;
import com.EGEA1R.CarService.entity.Credential;
import com.EGEA1R.CarService.entity.VerificationToken;
import com.EGEA1R.CarService.events.OnRegistrationCompleteEvent;
import com.EGEA1R.CarService.payload.request.LoginRequest;
import com.EGEA1R.CarService.payload.request.SignupRequest;
import com.EGEA1R.CarService.payload.response.JwtResponse;
import com.EGEA1R.CarService.payload.response.MessageResponse;
import com.EGEA1R.CarService.security.jwt.JwtUtils;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.CredentialService;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.PasswordresetTokenService;
import com.EGEA1R.CarService.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class CredentialController {


    private AuthenticationManager authenticationManager;

    private CredentialService credentialService;

    private JwtUtils jwtUtils;

    private ApplicationEventPublisher applicationEventPublisher;

    private VerificationTokenService verificationTokenService;

    private EmailService emailService;

    private PasswordresetTokenService passwordresetTokenService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService){
        this.credentialService = credentialService;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils){
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
            return ResponseEntity.ok(new MessageResponse("Invalid token"));
        }

        Credential credential = verificationToken.getCredential();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.ok(new MessageResponse("redirect to bad user html, authentication token expired"));
        }

        credential.setPermission("ROLE_USER");
        verificationTokenService.modifyPermissionOnVerificatedUser(credential);
        return ResponseEntity.ok(new MessageResponse("Successfully verificated"));
    }

    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<?> resendRegistrationToken(
             @RequestParam("token") String existingToken) {
        VerificationToken newToken = verificationTokenService.generateNewVerificationToken(existingToken);

        Credential credential = verificationTokenService.getCredential(newToken.getToken());
        String recipientAddress = credential.getEmail();
        emailService.resendVerificationToken(recipientAddress, newToken.getToken());
        return ResponseEntity.ok(new MessageResponse("Verification token resent"));
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam("email") String userEmail) {
        Optional<Credential> credential = credentialService.getByEmail(userEmail);
        if (credential == null) {
         //   throw new UserNotFoundException();
        }
        credential.ifPresent(credential1 -> {
        String token = UUID.randomUUID().toString();
        String recipientAddress = credential.get().getEmail();
        credentialService.createPasswordResetTokenForCredential(credential1, token);
        emailService.sendResetPasswordToken(recipientAddress, token);
        });
        return ResponseEntity.ok(new MessageResponse("Password reset token sent"));
    }

    @GetMapping("/user/changePassword")
    public ResponseEntity<?> showChangePasswordPage(
                                         @RequestParam("token") String token) {
        String result = passwordresetTokenService.validatePasswordResetToken(token);
        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("Invalid or expired password reset token"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Redirect to somewhere"));
        }
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(@Valid PasswordDTO passwordDto) {

        String result = passwordresetTokenService.validatePasswordResetToken(passwordDto.getToken());

        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("Invalid or expired password reset token"));
        }

        Optional<Credential> credential = credentialService.getCredentialByToken(passwordDto.getToken());
        if(credential.isPresent()) {
            credentialService.changeCredentialPassword(credential.get(), passwordDto.getPassword());
            return ResponseEntity.ok(new MessageResponse("Password changed"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Invalid password"));
        }
    }
}

