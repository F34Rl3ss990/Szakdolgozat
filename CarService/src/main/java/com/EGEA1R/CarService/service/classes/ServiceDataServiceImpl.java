package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.interfaces.*;
import com.EGEA1R.CarService.service.interfaces.DocumentService;
import com.EGEA1R.CarService.service.interfaces.ServiceDataService;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceDataServiceImpl implements ServiceDataService {

    private ServiceDataRepository serviceDataRepository;

    private DocumentService documentService;

    @Autowired
    public void setServiceDataRepository(ServiceDataRepository serviceDataRepository) {
        this.serviceDataRepository = serviceDataRepository;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Async
    @Transactional
    @Override
    public String saveDataAndFinance(HttpServletRequest request) {
       return documentService.storeClientBigFiles(request);
    }

    @Override
    public List<ServiceByUserResponse> getServiceDataListByUser(Long credentialId) {
        List<ServiceDataDTO> dbServiceList = serviceDataRepository.getAllServiceByUser(credentialId);
        List<ServiceByUserResponse> response = new ArrayList<>();
        Set<ServiceByUserResponse> placeholderSet = new HashSet<>();
        for (ServiceDataDTO serviceDataDTO : dbServiceList) {
            ServiceByUserResponse serviceByUserResponse = new ServiceByUserResponse();
            serviceByUserResponse.setBrand(serviceDataDTO.getBrand());
            serviceByUserResponse.setType(serviceDataDTO.getType());
            serviceByUserResponse.setLicensePlateNumber(serviceDataDTO.getLicensePlateNumber());
            serviceByUserResponse.setCarId(serviceDataDTO.getFkCarId());
            placeholderSet.add(serviceByUserResponse);
        }
        for (ServiceByUserResponse service : placeholderSet) {
            response.add(service);
        }
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
    public List<ServiceDataDTO> getServiceDataListByCar(Long carId, Long credentialId) {
        return serviceDataRepository.getAllServiceByCar(carId, credentialId);
    }

}
