package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
