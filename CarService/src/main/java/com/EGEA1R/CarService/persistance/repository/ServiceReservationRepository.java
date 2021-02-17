package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;

public interface ServiceReservationRepository extends JpaRepository<ServiceReservation, Long> {

    Page<ServiceReservation> findAllByReservedDate(LocalDate localDate, PageRequest pageRequest);
}
