package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Workday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkdayRepository extends JpaRepository<Workday, Long> {
}
