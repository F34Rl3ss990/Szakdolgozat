package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.InternalServerException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.*;
import com.EGEA1R.CarService.persistance.repository.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.PasswordResetTokenRepository;
import com.EGEA1R.CarService.persistance.repository.TokenBlockRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.security.jwt.JwtUtilId;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.CredentialService;
import com.EGEA1R.CarService.service.interfaces.JwtTokenCheckService;
import com.EGEA1R.CarService.service.interfaces.OTPService;
import com.EGEA1R.CarService.service.interfaces.TotpManager;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordEncoder passwordEncoder;

    private TokenBlockRepository tokenBlockRepository;

    private JwtUtilId jwtUtilId;

    private TotpManager totpManager;

    private OTPService otpService;

    private UserRepository userRepository;

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

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Boolean credentialExistByEmail(String email) {
        return credentialRepository.existsByEmail(email);
    }

    @Override
    public Credential getByEmail(String email) {
        return  credentialRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("username %s", email)));

    }

    @Override
    public String getMfa(String email){
        return getByEmail(email).getMfa();
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
    public Credential createNewCredential(String email, String password) {
        Credential credential = Credential.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        credentialRepository.save(credential);
        return credential;
    }

    @Override
    public void changePassword(final Credential credential, final String password) {
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
    public UsernamePasswordAuthenticationToken verify(String email, String code) {
        Credential credential = credentialRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("username %s", email)));
        String mfaType = credential.getMfa();
        if(mfaType.equals("phone")) {
            phoneVerify(code, credential);
        }else if(mfaType.equals("email")){
            emaiLVerify(code, email, credential);
        }
        throw new BadRequestException(String.format("Incorrect user:  %s", email));
    }

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
    }

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

    private UsernamePasswordAuthenticationToken phoneVerify(String code, Credential credential){
        if (!totpManager.verifyCode(code, credential.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }
        return Optional.of(credential)
                .map(AuthCredentialDetailsImpl::build)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .orElseThrow(() ->
                        new InternalServerException("unable to generate access token"));
    }

    private UsernamePasswordAuthenticationToken emaiLVerify(String code, String email, Credential credential){
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
}
