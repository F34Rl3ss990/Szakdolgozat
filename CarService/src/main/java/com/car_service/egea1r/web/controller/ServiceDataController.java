package com.car_service.egea1r.web.controller;

import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.service.interfaces.ServiceDataService;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/serviceData")
public class ServiceDataController {

    private final ServiceDataService serviceDataService;

    private final DocumentService documentService;

    @Autowired
    public ServiceDataController(ServiceDataService serviceDataService, DocumentService documentService) {
        this.serviceDataService = serviceDataService;
        this.documentService = documentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
    @PostMapping(value = "/save")
    public void saveServiceData(HttpServletRequest request) throws IOException, FileUploadException {
        documentService.storeClientBigFiles(request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/serviceDataByUser")
    public List<ServiceByUserResponse> getServiceDataByUser(@RequestParam Long credentialId){
       return serviceDataService.getServiceDataListByUser(credentialId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/serviceDataByCar")
    public List<ServiceDataDTO> getServiceDataByCar(@RequestParam Long carId, @RequestParam Long credentialId){
        return serviceDataService.getServiceDataListByCar(carId, credentialId);
    }

}
