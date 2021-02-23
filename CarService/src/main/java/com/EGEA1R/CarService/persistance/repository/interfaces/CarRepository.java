package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;


public interface CarRepository {

    void addCar(Car car);

    void modifyCarById(Car car);

    void deleteCarById(Long carId);

    List<Car> getAllCarByUser(Long userId);

    Optional<Car> finCarByCarId(Long carId);
}
