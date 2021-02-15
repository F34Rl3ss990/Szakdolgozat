package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByCredential(Credential credential);
}
