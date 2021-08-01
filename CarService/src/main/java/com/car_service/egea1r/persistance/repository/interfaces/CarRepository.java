package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.Car;

import java.util.List;
import java.util.Optional;


public interface CarRepository {

    void addCar(Car car, String mileage, long userId);

    void modifyCarById(Car car);

    List<Car> getAllCarByUser(long userId);

    Optional<Car> finCarByCarId(long carId);

}
