package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;

import java.util.List;

public interface ServiceDataRepository {

    Long saveServiceData(ServiceDataDTO serviceDataDTO, Long financeId);

    List<ServiceDataDTO> getAllServiceByUser(Long credentialId);

    List<ServiceDataDTO> getAllServiceByCar(Long carId, Long credentialId);
}
