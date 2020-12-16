package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialServiceImpl implements CredentialService{

    private CredentialRepository credentialRepository;

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }
}
