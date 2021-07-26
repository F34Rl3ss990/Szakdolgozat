package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.web.DTO.CarDTO;

import java.util.List;

public interface CarService {

    void addCar(CarDTO carDTO, Long credentialId);

    void modifyCar(CarDTO carDTO);

    List<Car> getAllCarListByUser(Long userId);

    Car getCarById(Long id);
}
