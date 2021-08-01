package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
