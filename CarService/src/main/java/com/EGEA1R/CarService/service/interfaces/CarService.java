package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.web.DTO.CarDTO;

import java.util.List;

public interface CarService {

    void addCar(CarDTO carDTO);

    void modifyCar(CarDTO carDTO);

    void deleteCar(Long id);

    List<Car> getAllCarListByUser(Long userId);

    Car getCarById(Long id);
}
