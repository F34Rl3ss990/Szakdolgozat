package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.InternalServerException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.PasswordResetToken;
import com.EGEA1R.CarService.persistance.entity.TokenBlock;
import com.EGEA1R.CarService.persistance.repository.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.PasswordResetTokenRepository;
import com.EGEA1R.CarService.persistance.repository.TokenBlockRepository;
import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.security.jwt.JwtUtilId;
import com.EGEA1R.CarService.security.jwt.JwtUtilsImpl;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.CredentialService;
import com.EGEA1R.CarService.service.interfaces.JwtTokenCheckService;
import com.EGEA1R.CarService.service.interfaces.OTPService;
import com.EGEA1R.CarService.service.interfaces.TotpManager;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class CredentialServiceImpl implements CredentialService, JwtTokenCheckService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private CredentialRepository credentialRepository;

    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordEncoder passwordEncoder;

    private TokenBlockRepository tokenBlockRepository;

    private JwtUtilId jwtUtilId;

    private TotpManager totpManager;

    private OTPService otpService;

    @Autowired
    public void setJwtUtilId(JwtUtilId jwtUtilId){
        this.jwtUtilId = jwtUtilId;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Autowired
    public void setPasswordResetTokenRepository(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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

    @Override
    public Boolean credentialExistByEmail(String email) {
        return credentialRepository.existsByEmail(email);
    }

    @Override
    public Credential getByEmail(String email) {
        Credential credential = credentialRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("username %s", email)));;
        return credential;
    }

    @Override
    public String getMfa(String email){
        return getByEmail(email).getMfa();
    }

    @Override
    public UsernamePasswordAuthenticationToken verify(String email, String code) {
        Credential credential = credentialRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("username %s", email)));
        String mfaType = credential.getMfa();
        if(mfaType.equals("phone")) {
            if (!totpManager.verifyCode(code, credential.getSecret())) {
                throw new BadRequestException("Code is incorrect");
            }
            return Optional.of(credential)
                    .map(AuthCredentialDetailsImpl::build)
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()))
                    .orElseThrow(() ->
                            new InternalServerException("unable to generate access token"));

        }else if(mfaType.equals("email")){
            int otpNum = Integer.parseInt(code);
            if(otpNum >= 0){
                int serverOtp = otpService.getOtp(email);
                if(serverOtp > 0){
                    if(otpNum == serverOtp){
                        otpService.clearOTP(email);
                        return Optional.of(credential)
                                .map(AuthCredentialDetailsImpl::build)
                                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()))
                                .orElseThrow(() ->
                                        new InternalServerException("unable to generate access token"));
                    }
                    else {
                        return null;
                        //exception
                    }
                }else {
                    return null;
                    //exception
                }
            }else {
                return null;
                //exception
            }
        }
        return null;
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
        credentialRepository.save(credential);
        return credential;
    }


    @Override
    public void createPasswordResetTokenForCredential(Credential credential, String token) {
        PasswordResetToken myToken = PasswordResetToken.builder()
                .credential(credential)
                .token(token)
                .expiryDate(calculateExpiryDate())
                .build();
                passwordResetTokenRepository.save(myToken);
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
        return Optional.of(passwordResetTokenRepository.findByToken(token).getCredential());
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

    @Override
    public void saveBlockedToken(HttpServletRequest request){
        String jwt = parseJwt(request);
        jwt = EncrypterHelper.decrypt(jwt);
        String email = jwtUtilId.getEmailFromJwtToken(jwt);
        Long id = getByEmail(email).getCredential_id();

        TokenBlock tokenBlock = TokenBlock.builder()
                .userId(id)
                .jwtToken(jwt)
                .build();
        tokenBlockRepository.save(tokenBlock);
    }

    @Override
    public Optional<TokenBlock> findTokenBlock(String email){
        Optional<TokenBlock> tokenBlock = tokenBlockRepository.findByUserId(getByEmail(email).getCredential_id());
        return tokenBlock;
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
}
