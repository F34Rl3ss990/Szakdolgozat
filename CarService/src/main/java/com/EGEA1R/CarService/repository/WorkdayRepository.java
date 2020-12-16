package com.EGEA1R.CarService.repository;

import com.EGEA1R.CarService.entity.Workday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkdayRepository extends JpaRepository<Workday, Integer> {
}
