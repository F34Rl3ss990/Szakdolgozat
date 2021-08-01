package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.ServiceReservation;
import com.car_service.egea1r.persistance.entity.User;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUnAuthorizedUser(User user, Car car, ServiceReservation serviceReservation);

    void modifyUserData(User user, long userId);

    Optional<User> findUserByCredentialId(long credentialId);

    List<User> getAllUser();

    Optional<UserDataDTO> findUserByCarId(long carId);

    User getUserByCarId(long carId);

    void modifyPhoneNumber(String phoneNumber, long userId);

    void addCarAndUser(CarAndUserDTO carAndUserDTO, long credentialId, String email);

    long findUserIdByCredentialId(long credentialId);
}
