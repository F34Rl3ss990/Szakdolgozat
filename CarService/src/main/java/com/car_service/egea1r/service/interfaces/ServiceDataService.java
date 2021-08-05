package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ServiceDataService {

    void saveDataAndFinance(HttpServletRequest request) throws IOException, FileUploadException;

    Set<ServiceByUserResponse> getServiceDataListByUser(long credentialId);

    List<ServiceDataDTO> getServiceDataListByCar(long carId, long credentialId);
}
