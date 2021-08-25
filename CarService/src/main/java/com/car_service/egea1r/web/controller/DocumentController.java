package com.car_service.egea1r.web.controller;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.service.interfaces.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS')")
    @PostMapping(value="/uploadBigFile")
    public ResponseEntity<String> upload(@RequestParam("file")MultipartFile[] files) {
        try{
            List<String> fileNames = documentService.uploadService(files);
            String message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.ok(message);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Fail to upload files!");
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS', 'USER')")
    @GetMapping("/filesCarId")
    public ResponseEntity<Set<FileAndCarResponse>> getListFilesByCarId(@RequestParam("carId") Long carId, @RequestParam("credentialId") Long credentialId){
        return ResponseEntity.ok(documentService.getAllFilesByCar(carId, credentialId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS', 'USER')")
    @GetMapping("/filesByUser")
    public ResponseEntity<Set<FileAndCarResponse>> getListFilesByCredentialId(@RequestParam("credentialId") Long credentialId){
        return ResponseEntity.ok(documentService.getAllFilesByUser(credentialId));
    }

    @PostMapping(value = "/zip-download", produces="application/zip")
    public void zipFiles(@RequestParam("id") List<Long> fileId, HttpServletResponse response) throws IOException{
        Document document = documentService.findDocument(fileId.get(0));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sdf.format(new Date()) + " " + document.getUploadTime() + ".zip" + "\"");
        documentService.zippingFiles(fileId, response);

    }

}
