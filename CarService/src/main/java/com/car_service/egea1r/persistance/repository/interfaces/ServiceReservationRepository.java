package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.ServiceReservation;

import java.time.LocalDate;
import java.util.List;

public interface ServiceReservationRepository {

    void saveService(ServiceReservation serviceReservation);

    List<ServiceReservation> getAllServiceByUser(long userId);

    List<ServiceReservation> getAllServiceByTodayDate(LocalDate localDate);

    List<Car> getAllCarByCredentialId(long credentialId);

    void setServiceDataFk(long serviceDataFk, long carId);
}
