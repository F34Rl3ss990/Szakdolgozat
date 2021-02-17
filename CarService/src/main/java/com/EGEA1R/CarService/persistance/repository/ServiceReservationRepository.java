package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceReservation, Long> {
}
