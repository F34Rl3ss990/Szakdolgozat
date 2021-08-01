package com.car_service.egea1r.security.jwt;

import org.springframework.security.core.Authentication;

public interface JwtUtils extends JwtUtilId{

    String generateJwtToken(Authentication authentication);

    void setJwtExpirationMs(String token);

    boolean checkIfNotBlocked(String authToken);

    boolean validateJwtToken(String authToken);
}
