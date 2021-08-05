package com.car_service.egea1r.security.jwt;

import com.car_service.egea1r.persistance.entity.TokenBlock;
import com.car_service.egea1r.service.authentication.AuthCredentialDetailsImpl;
import com.car_service.egea1r.service.interfaces.JwtTokenCheckService;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.List;

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
    public void setJwtUtilsImpl(JwtTokenCheckService jwtTokenCheckService) {
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
    public boolean checkIfNotBlocked(String authToken) {
        String email = getEmailFromJwtToken(authToken);
        List<TokenBlock> tokenBlock = jwtTokenCheckService.findTokenBlock(email);
        boolean tokenNotEqual = true;
        for (TokenBlock tokens : tokenBlock) {
            if (tokens.getJwtToken().equals(authToken)) {
                tokenNotEqual = false;
                break;
            }
        }
        return tokenNotEqual;
    }

    @Override
    public boolean validateJwtToken(String authToken) {
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
