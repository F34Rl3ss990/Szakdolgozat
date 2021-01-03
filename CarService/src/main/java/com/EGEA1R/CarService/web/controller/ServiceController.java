package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.interfaces.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ServiceController {

    private ServiceService serviceService;

    @Autowired
    public void setServiceService(ServiceService serviceService){
        this.serviceService = serviceService;
    }
}
