package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;

import java.util.List;

public interface ServiceDataRepository {

    Long saveServiceData(ServiceDataDTO serviceDataDTO, Long financeId);

    List<ServiceDataDTO> getAllServiceByUser(Long credentialId);

    List<ServiceDataDTO> getAllServiceByCar(Long carId);
}
