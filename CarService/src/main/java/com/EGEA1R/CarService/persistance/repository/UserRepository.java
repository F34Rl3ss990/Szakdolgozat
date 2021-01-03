package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
