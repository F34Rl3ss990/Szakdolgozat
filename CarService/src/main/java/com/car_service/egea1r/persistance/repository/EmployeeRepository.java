package com.car_service.egea1r.persistance.repository;

import com.car_service.egea1r.persistance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
