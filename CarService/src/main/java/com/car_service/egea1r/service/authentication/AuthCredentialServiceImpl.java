package com.car_service.egea1r.service.authentication;

import com.car_service.egea1r.persistance.entity.Credential;
import com.car_service.egea1r.persistance.repository.interfaces.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthCredentialServiceImpl implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    @Autowired
    public AuthCredentialServiceImpl(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return AuthCredentialDetailsImpl.build(credential);
    }

}

