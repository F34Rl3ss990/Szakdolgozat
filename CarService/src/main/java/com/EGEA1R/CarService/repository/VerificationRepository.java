package com.EGEA1R.CarService.repository;

import com.EGEA1R.CarService.entity.Credential;
import com.EGEA1R.CarService.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByCredential(Credential credential);


}
