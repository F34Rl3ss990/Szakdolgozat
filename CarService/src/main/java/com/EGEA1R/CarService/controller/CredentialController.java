package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.CredentialService;
import com.EGEA1R.CarService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CredentialController {

    private CredentialService credentialService;
    private UserService userService;

    @Autowired
    public void setCredentialService(CredentialService credentialService){
        this.credentialService = credentialService;
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

}
