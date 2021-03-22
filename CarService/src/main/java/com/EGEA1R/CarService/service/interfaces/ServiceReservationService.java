package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import com.EGEA1R.CarService.web.DTO.UserCarsDTO;
import org.springframework.beans.support.PagedListHolder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

public interface ServiceReservationService {

    void saveService(ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException;

    PagedListHolder<ServiceReservation> getServicesByUserOrderByDate(int page, int size, Long userId);

    PagedListHolder<ServiceReservation> getServicesTodayOrderByDate(int page, int size);

    List<UserCarsDTO> getCarByCredentialId(Long credentialId) throws ParseException;
}
