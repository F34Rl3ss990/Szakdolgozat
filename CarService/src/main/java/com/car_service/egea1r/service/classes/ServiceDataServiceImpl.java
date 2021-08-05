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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceDataServiceImpl implements ServiceDataService {

    private final ServiceDataRepository serviceDataRepository;
    private final DocumentService documentService;
    private final MapStructObjectMapper mapStructObjectMapper;

    @Autowired
    public ServiceDataServiceImpl(ServiceDataRepository serviceDataRepository, DocumentService documentService, MapStructObjectMapper mapStructObjectMapper) {
        this.serviceDataRepository = serviceDataRepository;
        this.documentService = documentService;
        this.mapStructObjectMapper = mapStructObjectMapper;
    }

    @Async
    @Transactional
    @Override
    public void saveDataAndFinance(HttpServletRequest request) throws IOException, FileUploadException {
        documentService.storeClientBigFiles(request);
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
                if (service.getCarId() == listOfServiceData.getFkCarId()) {
                    ServiceDataDTO serviceData = ServiceDataDTO.builder()
                            .date(listOfServiceData.getDate())
                            .amount(listOfServiceData.getAmount())
                            .billNum(listOfServiceData.getBillNum())
                            .servicesDone(listOfServiceData.getServicesDone())
                            .comment(listOfServiceData.getComment())
                            .mileage(listOfServiceData.getMileage())
                            .build();
                    dataHolderForResponse.add(serviceData);
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
