package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.interfaces.DocumentService;
import com.EGEA1R.CarService.service.interfaces.ServiceDataService;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/serviceData")
public class ServiceDataController {

    private ServiceDataService serviceDataService;

    private DocumentService documentService;

    @Autowired
    public void setServiceDataService(ServiceDataService serviceDataService){
        this.serviceDataService = serviceDataService;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService){
        this.documentService = documentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
    @PostMapping(value = "/save")
    public MessageResponse saveServiceData(HttpServletRequest request){
        String message =  documentService.storeClientBigFiles(request);
        return new MessageResponse(message);
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
