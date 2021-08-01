package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.service.interfaces.ServiceDataService;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
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

    @Autowired
    public ServiceDataServiceImpl(ServiceDataRepository serviceDataRepository, DocumentService documentService) {
        this.serviceDataRepository = serviceDataRepository;
        this.documentService = documentService;
    }

    @Async
    @Transactional
    @Override
    public void saveDataAndFinance(HttpServletRequest request) throws IOException, FileUploadException {
        documentService.storeClientBigFiles(request);
    }

    @Override
    public List<ServiceByUserResponse> getServiceDataListByUser(long credentialId) {
        List<ServiceDataDTO> dbServiceList = serviceDataRepository.getAllServiceByUser(credentialId);
        Set<ServiceByUserResponse> placeholderSet = new HashSet<>();
        for (ServiceDataDTO serviceDataDTO : dbServiceList) {
            ServiceByUserResponse serviceByUserResponse = new ServiceByUserResponse();
            serviceByUserResponse.setBrand(serviceDataDTO.getBrand());
            serviceByUserResponse.setType(serviceDataDTO.getType());
            serviceByUserResponse.setLicensePlateNumber(serviceDataDTO.getLicensePlateNumber());
            serviceByUserResponse.setCarId(serviceDataDTO.getFkCarId());
            placeholderSet.add(serviceByUserResponse);
        }
        List<ServiceByUserResponse> response = new ArrayList<>(placeholderSet);
        for (ServiceByUserResponse service : response) {
            List<ServiceDataDTO> dataHolderForResponse = new ArrayList<>();
            for (ServiceDataDTO listOfServiceData : dbServiceList) {
                if (service.getCarId() == listOfServiceData.getFkCarId()) {
                    ServiceDataDTO serviceData = new ServiceDataDTO();
                    serviceData.setDate(listOfServiceData.getDate());
                    serviceData.setAmount(listOfServiceData.getAmount());
                    serviceData.setBillNum(listOfServiceData.getBillNum());
                    serviceData.setServicesDone(listOfServiceData.getServicesDone());
                    serviceData.setComment(listOfServiceData.getComment());
                    serviceData.setMileage(listOfServiceData.getMileage());
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
