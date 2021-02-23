package com.EGEA1R.CarService.security.jwt;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.JwtTokenCheckService;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilsImpl implements JwtUtils, JwtUtilId{

    private static final Logger logger = LoggerFactory.getLogger(JwtUtilsImpl.class);

    private JwtTokenCheckService jwtTokenCheckService;

    @Autowired
    public void setJwtTokenCheckService(JwtTokenCheckService jwtTokenCheckService){
        this.jwtTokenCheckService = jwtTokenCheckService;
    }

    @Value("${CarService.app.jwtSecret}")
    private String jwtSecret;

    @Value("${CarService.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Override
    public String generateJwtToken(Authentication authentication) {

        AuthCredentialDetailsImpl userPrincipal = (AuthCredentialDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public void setJwtExpirationMs(String token) {
         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().setExpiration(new Date((new Date()).getTime() + jwtExpirationMs));
    }

    @Override
    public Boolean checkIfNotBlocked(String authToken){
        String email = getEmailFromJwtToken(authToken);
        Optional<TokenBlock> tokenBlock = jwtTokenCheckService.findTokenBlock(email);
        if(tokenBlock.isPresent()) {
            return !tokenBlock.get().getJwtToken().equals(authToken);
        }else{
            return true;
        }
    }

    @Override
    public Boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
