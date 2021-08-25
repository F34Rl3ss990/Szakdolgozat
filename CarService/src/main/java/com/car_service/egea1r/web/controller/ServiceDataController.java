package com.car_service.egea1r.web.controller;

import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.service.interfaces.ServiceDataService;
import com.car_service.egea1r.web.data.DTO.FinanceDTO;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

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

    @PostMapping(value = "/uploadFiles")
    public ResponseEntity<String> testMediaPart(@RequestPart("file") MultipartFile[] files, @RequestPart ServiceDataDTO serviceDataDTO,
                                                @RequestPart FinanceDTO financeDTO, @RequestPart String email){
        try{
            List<String> fileNames = documentService.upload(files, serviceDataDTO, financeDTO, email);
            String message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.ok(message);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Fail to upload files!");
        }
    }

    @GetMapping("/files2/{id:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable long id) throws IOException {
        Resource file = documentService.download(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.contentLength()));
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(file);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/serviceDataByUser")
    public Set<ServiceByUserResponse> getServiceDataByUser(@RequestParam Long credentialId){
       return serviceDataService.getServiceDataListByUser(credentialId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/serviceDataByCar")
    public List<ServiceDataDTO> getServiceDataByCar(@RequestParam Long carId, @RequestParam Long credentialId){
        return serviceDataService.getServiceDataListByCar(carId, credentialId);
    }

}
