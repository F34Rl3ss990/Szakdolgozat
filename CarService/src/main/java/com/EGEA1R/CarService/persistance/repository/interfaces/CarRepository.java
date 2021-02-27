package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;

import java.util.List;
import java.util.Optional;


public interface CarRepository {

    void addCar(Car car);

    void modifyCarById(Car car);

    void deleteCarById(Long carId);

    List<Car> getAllCarByUser(Long userId);

    Optional<Car> finCarByCarId(Long carId);
}
