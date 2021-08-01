package com.car_service.egea1r.web.controller;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.service.authentication.AuthCredentialDetailsImpl;
import com.car_service.egea1r.service.interfaces.CarService;
import com.car_service.egea1r.web.data.DTO.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/addCar")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addCarToUser(@Valid @RequestBody CarDTO carDTO) {
        AuthCredentialDetailsImpl credentialDetails = CredentialController.getAuthPrincipal();
        carService.addCar(carDTO, credentialDetails.getCredentialId());
    }

    @PostMapping("/modifyCar")
    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyCar(@Valid @RequestBody CarDTO carDTO) {
        carService.modifyCar(carDTO);
    }


    @GetMapping("/getCarById")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'BOSS')")
    @ResponseStatus(HttpStatus.OK)
    public Car getCarById(@RequestBody Long carId) {
        return carService.getCarById(carId);
    }
}
