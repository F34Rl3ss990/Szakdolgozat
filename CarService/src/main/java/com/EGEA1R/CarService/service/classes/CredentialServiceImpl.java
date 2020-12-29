package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.entity.Credential;
import com.EGEA1R.CarService.entity.PasswordresetToken;
import com.EGEA1R.CarService.repository.CredentialRepository;
import com.EGEA1R.CarService.repository.PasswordresetTokenRepository;
import com.EGEA1R.CarService.service.interfaces.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class CredentialServiceImpl implements CredentialService {

    private CredentialRepository credentialRepository;

    private PasswordresetTokenRepository passwordresetTokenRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Autowired
    public void setPasswordresetTokenRepository(PasswordresetTokenRepository passwordresetTokenRepository) {
        this.passwordresetTokenRepository = passwordresetTokenRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean credentialExistByEmail(String email) {
        return credentialRepository.existsByEmail(email);
    }

    @Override
    public Optional<Credential> getByEmail(String email) {
        Optional<Credential> credential = credentialRepository.findByEmail(email);
        return credential;
    }

    @Override
    public void createPasswordResetTokenForCredential(Optional<Credential> credential, String token) {
        PasswordresetToken myToken = PasswordresetToken.builder()
                .credential(credential.get())
                .token(token)
                .expiryDate(calculateExpiryDate())
                .build();
        ;
        passwordresetTokenRepository.save(myToken);
    }

    private Date calculateExpiryDate() {
        final int EXPIRATION = 60 * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public Optional<Credential> getCredentialByToken(String token) {
        return Optional.of(passwordresetTokenRepository.findByToken(token).getCredential());
    }

    @Override
    public void changeCredentialPassword(Credential credential1, String newPassword) {
        credential1.setPassword(passwordEncoder.encode(newPassword));
        credentialRepository.save(credential1);
    }

    @Override
    public Credential createNewCredential(String email, String password) {
        Credential credential = Credential.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        credentialRepository.save(credential);
        return credential;
    }

    @Override
    public void changeUserPassword(final Credential credential, final String password) {
        credential.setPassword(passwordEncoder.encode(password));
        credentialRepository.save(credential);
    }

    @Override
    public boolean checkIfValidOldPassword(final Credential credential, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, credential.getPassword());
    }

}
