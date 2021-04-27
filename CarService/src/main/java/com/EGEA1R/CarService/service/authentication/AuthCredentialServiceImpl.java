package com.EGEA1R.CarService.service.authentication;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthCredentialServiceImpl implements UserDetailsService {

    private CredentialRepository credentialRepository;

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return AuthCredentialDetailsImpl.build(credential);
    }

}

