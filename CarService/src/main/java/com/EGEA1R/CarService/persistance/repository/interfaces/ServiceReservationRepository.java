package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.security.Provider;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ServiceReservationRepository {

    void saveService(ServiceReservation serviceReservation, String services);

    List<ServiceReservation> getAllServiceByUser(Long userId);

    List<ServiceReservation> getAllServiceByTodayDate(LocalDate localDate);
}
