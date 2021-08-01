package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;

import java.util.List;

public interface ServiceDataRepository {

    long saveServiceData(ServiceDataDTO serviceDataDTO, long financeId);

    List<ServiceDataDTO> getAllServiceByUser(long credentialId);

    List<ServiceDataDTO> getAllServiceByCar(long carId, long credentialId);
}
