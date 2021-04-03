package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.persistance.entity.ServiceData;
import com.EGEA1R.CarService.persistance.repository.interfaces.*;
import com.EGEA1R.CarService.service.interfaces.DocumentService;
import com.EGEA1R.CarService.service.interfaces.ServiceDataService;
import com.EGEA1R.CarService.web.DTO.FinanceDTO;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.DocumentRequest;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceDataServiceImpl implements ServiceDataService {

    private ServiceReservationRepository serviceReservationRepository;

    private FinanceRepository financeRepository;

    private ServiceDataRepository serviceDataRepository;

    private DocumentService documentService;

    @Autowired
    public void setServiceReservationRepository(ServiceReservationRepository serviceReservationRepository) {
        this.serviceReservationRepository = serviceReservationRepository;
    }

    @Autowired
    public void setFinanceRepository(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

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
    public void saveDataAndFinance(List<MultipartFile> fileList, ServiceDataDTO serviceDataDTO, FinanceDTO financeDTO, String email) throws Exception {
        Long financeId = financeRepository.saveFinanceByServiceData(financeDTO);
        Long serviceDataId = serviceDataRepository.saveServiceData(serviceDataDTO, financeId);
        serviceReservationRepository.setServiceDataFk(serviceDataId, serviceDataDTO.getFkCarId());
        documentService.storeClientFiles(fileList, serviceDataDTO.getFkCarId(), email, serviceDataId);
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
    public List<ServiceDataDTO> getServiceDataListByCar(Long carId) {
        return serviceDataRepository.getAllServiceByCar(carId);
    }
/*
    @Override
    public List<ServiceByUserResponse> getServiceDataListByUser(Long credentialId) {
        List<ServiceDataDTO> listFromDb = serviceDataRepository.getAllServiceByUser(credentialId);
        List<ServiceDataDTO> populateList = new ArrayList<>();
        List<ServiceByUserResponse> response = new ArrayList<>();
        Long id = null;
        ServiceByUserResponse serviceByUserResponse = new ServiceByUserResponse();
        int i = 1;
        for (ServiceDataDTO serviceDataDTO : listFromDb) {
            ServiceDataDTO dataForList = new ServiceDataDTO();
            if (id == null) {
                serviceByUserResponse.setBrand(serviceDataDTO.getBrand());
                serviceByUserResponse.setType(serviceDataDTO.getType());
                serviceByUserResponse.setLicensePlateNumber(serviceDataDTO.getLicensePlateNumber());
                serviceByUserResponse.setCarId(serviceDataDTO.getFkCarId());
                id = serviceDataDTO.getFkCarId();
            }
            if (id != serviceDataDTO.getFkCarId()) {
                serviceByUserResponse.setServiceList(populateList);
                response.add(serviceByUserResponse);
                populateList.clear();
                serviceByUserResponse.setBrand(serviceDataDTO.getBrand());
                serviceByUserResponse.setType(serviceDataDTO.getType());
                serviceByUserResponse.setLicensePlateNumber(serviceDataDTO.getLicensePlateNumber());
                serviceByUserResponse.setCarId(serviceDataDTO.getFkCarId());
            }
            id = serviceDataDTO.getFkCarId();
            dataForList.setDate(serviceDataDTO.getDate());
            dataForList.setAmount(serviceDataDTO.getAmount());
            dataForList.setBillNum(serviceDataDTO.getBillNum());
            dataForList.setServicesDone(serviceDataDTO.getServicesDone());
            dataForList.setComment(serviceDataDTO.getComment());
            dataForList.setMileage(serviceDataDTO.getMileage());
            populateList.add(dataForList);
            if (listFromDb.size() == i) {
                serviceByUserResponse.setServiceList(populateList);
                response.add(serviceByUserResponse);
            }
            i++;
        }
        return response;
    }
*/

}
