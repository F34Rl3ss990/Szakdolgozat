package com.EGEA1R.CarService.repository;

import com.EGEA1R.CarService.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<Credential>getCredentialByPasswordresetToken(String token);

}
