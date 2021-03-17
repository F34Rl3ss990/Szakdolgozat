package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.entity.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUser(@Valid User user, Long credentialId, String email);

    void saveUnAuthorizedUser(User user, Car car, ServiceReservation serviceReservation);

    void modifyUserData(User user);

    Optional<User> findUserByCredentialId(Long credentialId);

    List<User> getAllUser();

    Optional<User> findUserByCarId(Long carId);
}
