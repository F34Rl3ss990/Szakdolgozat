package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;


public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByUser(User user);

    Page<Car> findAllByUser_UserId(Long id, PageRequest pageRequest);

    @Procedure("GET_ALL_CAR_BY_USER")
    List<Car> storedGetAllCarByUser(Long userId);

    @Procedure("GET_CAR_BY_CAR_ID")
    Car storedGetCarByCarId(Long carId);

}
