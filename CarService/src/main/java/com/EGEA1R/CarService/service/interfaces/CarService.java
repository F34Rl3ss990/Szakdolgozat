package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CarService {

    void addCar(CarDTO carDTO, Long userId);

    void modifyCar(CarDTO carDTO);

    void deleteCar(Long id);

    Page<Car> getAllCarPage(int page, int size);

    Page<Car> getAllCarPageByUser(int page, int size, Long id);

    List<Car> getAllCarListByUser(Long id);

    Optional<Car> getCarById(Long id);
}
