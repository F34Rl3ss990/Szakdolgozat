package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.web.DTO.FinanceDTO;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ServiceDataService {

    void saveDataAndFinance(List<MultipartFile> fileList, ServiceDataDTO serviceDataDTO, FinanceDTO financeDTO, String email) throws Exception;

    List<ServiceByUserResponse> getServiceDataListByUser(Long credentialId);

    List<ServiceDataDTO> getServiceDataListByCar(Long carId);
}
