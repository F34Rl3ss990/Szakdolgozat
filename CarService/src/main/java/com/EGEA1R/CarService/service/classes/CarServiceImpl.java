package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.CarRepository;
import com.EGEA1R.CarService.service.interfaces.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }
}
