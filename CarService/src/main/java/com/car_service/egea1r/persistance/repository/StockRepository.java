package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
