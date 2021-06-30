package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.web.DTO.CarAndUserDTO;
import com.EGEA1R.CarService.web.DTO.payload.UserDataDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUnAuthorizedUser(User user, Car car, ServiceReservation serviceReservation);

    void modifyUserData(User user, Long userId);

    Optional<User> findUserByCredentialId(Long credentialId);

    List<User> getAllUser();

    Optional<UserDataDTO> findUserByCarId(Long carId);

    User getUserByCarId(Long carId);

    void modifyPhoneNumber(String phoneNumber, Long userId);

    void addCarAndUser(CarAndUserDTO carAndUserDTO, Long credentialId, String email);

    Long findUserIdByCredentialId(Long credentialId);
}
