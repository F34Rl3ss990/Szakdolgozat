package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import org.springframework.beans.support.PagedListHolder;

public interface ServiceReservationService {

    void saveService(ServiceReservationDTO serviceReservationDTO);

    PagedListHolder<ServiceReservation> getServicesByUserOrderByDate(int page, int size, Long userId);

    PagedListHolder<ServiceReservation> getServicesTodayOrderByDate(int page, int size);
}
