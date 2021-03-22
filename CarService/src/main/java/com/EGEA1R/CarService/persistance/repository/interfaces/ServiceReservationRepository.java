package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;

import java.time.LocalDate;
import java.util.List;

public interface ServiceReservationRepository {

    void saveService(ServiceReservation serviceReservation);

    List<ServiceReservation> getAllServiceByUser(Long userId);

    List<ServiceReservation> getAllServiceByTodayDate(LocalDate localDate);

    List<Car> getAllCarByCredentialId(Long credentialId);
}
