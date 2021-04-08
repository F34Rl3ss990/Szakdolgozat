package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ServiceDataService {

    String saveDataAndFinance(HttpServletRequest request);

    List<ServiceByUserResponse> getServiceDataListByUser(Long credentialId);

    List<ServiceDataDTO> getServiceDataListByCar(Long carId, Long credentialId);
}
