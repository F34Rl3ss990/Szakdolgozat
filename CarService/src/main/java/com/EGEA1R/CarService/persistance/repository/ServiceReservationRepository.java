package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.security.Provider;
import java.time.LocalDate;
import java.util.Date;

public interface ServiceReservationRepository extends JpaRepository<ServiceReservation, Long> {

    Page<ServiceReservation> findAllByReservedDate(LocalDate localDate, PageRequest pageRequest);

    @Procedure("GET_SERVICES_BY_USER_ORDER_BY_DATE")
    UnauthorizedUserReservationDTO storedGetServicesByUerOrderByDate();

    @Procedure("GET_SERVICES_TODAY")
    UnauthorizedUserReservationDTO storedGetServicesToday();

    @Procedure("SAVE_SERVICE_RESERVATION")
    void storedSaveServiceReservation();

    @Procedure("SAVE_UNAUTHORIZED_USER")
    void storedSaveUnauthorizedUser();
}
