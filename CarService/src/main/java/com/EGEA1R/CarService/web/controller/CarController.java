package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.interfaces.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CarController {

    private CarService carService;

    @Autowired
    public void setCarService(CarService carService){
        this.carService = carService;
    }
}
