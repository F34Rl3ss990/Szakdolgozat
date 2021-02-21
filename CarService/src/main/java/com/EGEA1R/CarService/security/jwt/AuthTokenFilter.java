package com.EGEA1R.CarService.security.jwt;

import com.EGEA1R.CarService.security.EncrypterHelper;
import com.EGEA1R.CarService.service.authentication.AuthCredentialServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    private AuthCredentialServiceImpl credentialService;
    private JwtUtilsImpl jwtUtils;

    @Autowired
    public void setJwtUtils(JwtUtilsImpl jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setCredentialService(AuthCredentialServiceImpl credentialService){
        this.credentialService = credentialService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            jwt = EncrypterHelper.decrypt(jwt);
            if(jwt != null && jwtUtils.validateJwtToken(jwt) && jwtUtils.checkIfNotBlocked(jwt)){
                String email = jwtUtils.getEmailFromJwtToken(jwt);
                jwtUtils.setJwtExpirationMs(jwt);
                UserDetails userDetails = credentialService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            }
        catch (Exception e){
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
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
