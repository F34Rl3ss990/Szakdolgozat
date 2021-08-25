package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.service.interfaces.ServiceDataService;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import com.car_service.egea1r.persistance.repository.interfaces.ServiceDataRepository;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class ServiceDataServiceImpl implements ServiceDataService {

    private final ServiceDataRepository serviceDataRepository;
    private final MapStructObjectMapper mapStructObjectMapper;

    @Autowired
    public ServiceDataServiceImpl(ServiceDataRepository serviceDataRepository, MapStructObjectMapper mapStructObjectMapper) {
        this.serviceDataRepository = serviceDataRepository;
        this.mapStructObjectMapper = mapStructObjectMapper;
    }


    @Override
    public Set<ServiceByUserResponse> getServiceDataListByUser(long credentialId) {
        List<ServiceDataDTO> dbServiceList = serviceDataRepository.getAllServiceByUser(credentialId);
        Set<ServiceByUserResponse> response = new HashSet<>();
        for (ServiceDataDTO serviceDataDTO : dbServiceList) {
            ServiceByUserResponse serviceByUserResponse = mapStructObjectMapper.serviceDataDTOtoServiceByUserResponse(serviceDataDTO);
            response.add(serviceByUserResponse);
        }
        for (ServiceByUserResponse service : response) {
            List<ServiceDataDTO> dataHolderForResponse = new ArrayList<>();
            for (ServiceDataDTO listOfServiceData : dbServiceList) {
                if (Objects.equals(service.getCarId(), listOfServiceData.getFkCarId())) {
                    dataHolderForResponse.add(listOfServiceData);
                }
            }
            service.setServiceList(dataHolderForResponse);
        }
        return response;
    }

    @Override
    public List<ServiceDataDTO> getServiceDataListByCar(long carId, long credentialId) {
        return serviceDataRepository.getAllServiceByCar(carId, credentialId);
    }

}
