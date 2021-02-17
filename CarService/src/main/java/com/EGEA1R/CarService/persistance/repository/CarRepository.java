package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;




public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByUser(User user);

    Page<Car> findAllByUser_UserId(Long id, PageRequest pageRequest);
}
