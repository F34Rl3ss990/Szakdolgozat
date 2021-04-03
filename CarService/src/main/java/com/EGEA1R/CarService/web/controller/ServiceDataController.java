package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.service.interfaces.ServiceDataService;
import com.EGEA1R.CarService.web.DTO.FinanceDTO;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.ServiceByUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/serviceData")
public class ServiceDataController {

    private ServiceDataService serviceDataService;

    @Autowired
    public void setServiceDataService(ServiceDataService serviceDataService){
        this.serviceDataService = serviceDataService;
    }

    @PostMapping(value = "/save", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveServiceData(@RequestPart("file") List<MultipartFile> fileList,  @RequestPart ServiceDataDTO serviceDataDTO, @RequestPart FinanceDTO financeDTO, String email) throws Exception {
        serviceDataService.saveDataAndFinance(fileList, serviceDataDTO, financeDTO, email);
        return ResponseEntity.ok(new MessageResponse("Kérés teljesítve"));
    }

    @GetMapping("/serviceDataByUser")
    public List<ServiceByUserResponse> getServiceDataByUser(@RequestBody Long credentialId){
       return serviceDataService.getServiceDataListByUser(credentialId);
    }

    @GetMapping("/serviceDataByCar")
    public List<ServiceDataDTO> getServiceDataByCar(@RequestBody Long carId){
        return serviceDataService.getServiceDataListByCar(carId);
    }

}
