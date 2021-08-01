package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
