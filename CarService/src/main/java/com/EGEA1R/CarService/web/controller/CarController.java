package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.interfaces.CarService;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/car")
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private CarService carService;

    @Autowired
    public void setCarService(CarService carService){
        this.carService = carService;
    }

    @PostMapping("/addCar")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addCarToUser(@Valid @RequestBody CarDTO carDTO){
        carService.addCar(carDTO);
        return ResponseEntity.ok("Car successfully added!");
    }

    @PostMapping("/modifyCar")
   // @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'BOSS')")
    public ResponseEntity<?> modifyCar(@Valid @RequestBody CarDTO carDTO){
        carService.modifyCar(carDTO);
        return ResponseEntity.ok("Car successfully modified!");
    }

    @DeleteMapping("/deleteCar")
   // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteCar(@RequestBody Long carId){
        carService.deleteCar(carId);
        return ResponseEntity.ok("Car successfully deleted!");
    }

    @GetMapping("/getEveryCarByUser")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'BOSS')")
    public ResponseEntity<?> getAllCarByUser(@RequestBody Long userId){
      return  ResponseEntity.ok(carService.getAllCarListByUser(userId));
    }

    @GetMapping("/getCarById")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'BOSS')")
    public ResponseEntity<?> getCarById(@RequestBody Long carId){
        return ResponseEntity.ok(carService.getCarById(carId));
    }
}
