package com.EGEA1R.CarService.repository;

import com.EGEA1R.CarService.entity.PasswordresetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordresetTokenRepository extends JpaRepository<PasswordresetToken, Long> {

    PasswordresetToken findByToken(String token);

}
