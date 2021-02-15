package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByUser(User user);

}
