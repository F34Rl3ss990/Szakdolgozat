package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByCredential(Credential credential);

    @Procedure("GET_USER_BY_CREDENTIAL_ID")
    Optional<User> storedGetUserByCredentialId(Long credentialId);

    @Procedure("GET_ALL_USER")
    List<User> storedGetAllUser();
}
