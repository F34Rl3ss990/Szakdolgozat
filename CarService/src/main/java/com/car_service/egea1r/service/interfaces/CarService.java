package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.web.data.DTO.CarDTO;

import java.util.List;

public interface CarService {

    void addCar(CarDTO carDTO, long credentialId);

    void modifyCar(CarDTO carDTO);

    List<Car> getAllCarListByUser(long userId);

    Car getCarById(long id);
}
