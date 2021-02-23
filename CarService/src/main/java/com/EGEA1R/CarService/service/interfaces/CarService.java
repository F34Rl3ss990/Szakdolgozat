package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CarService {

    void addCar(CarDTO carDTO);

    void modifyCar(CarDTO carDTO);

    void deleteCar(Long id);

//    Page<Car> getAllCarPage(int page, int size);

    List<Car> getAllCarListByUser(Long userId);

    Car getCarById(Long id);
}
