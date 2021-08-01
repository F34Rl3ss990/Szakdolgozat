package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
}
