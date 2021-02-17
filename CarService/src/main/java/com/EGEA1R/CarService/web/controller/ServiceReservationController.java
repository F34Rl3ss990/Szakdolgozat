package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.interfaces.ServiceReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ServiceReservationController {

    private ServiceReservationService serviceReservationService;

    @Autowired
    public void setServiceService(ServiceReservationService serviceReservationService){
        this.serviceReservationService = serviceReservationService;
    }
}
