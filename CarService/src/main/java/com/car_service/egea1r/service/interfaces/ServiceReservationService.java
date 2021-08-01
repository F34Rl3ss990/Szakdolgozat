package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.ServiceReservation;
import com.car_service.egea1r.web.data.DTO.ServiceReservationDTO;
import com.car_service.egea1r.web.data.DTO.UserCarsDTO;
import org.springframework.beans.support.PagedListHolder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

public interface ServiceReservationService {

    void saveService(ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException;

    PagedListHolder<ServiceReservation> getServicesByUserOrderByDate(int page, int size, long userId);

    PagedListHolder<ServiceReservation> getServicesTodayOrderByDate(int page, int size);

    List<UserCarsDTO> getCarByCredentialId(long credentialId) throws ParseException;
}
