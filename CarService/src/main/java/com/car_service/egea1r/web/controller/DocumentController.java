package com.car_service.egea1r.web.controller;

import java.io.*;
import java.util.List;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.service.interfaces.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    public String upload(HttpServletRequest request) {
        return documentService.storeServiceBigFiles(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS', 'USER')")
    @GetMapping("/filesCarId")
    public ResponseEntity<List<FileAndCarResponse>> getListFilesByCarId(@RequestParam("carId") Long carId, @RequestParam("credentialId") Long credentialId){
        return ResponseEntity.ok(documentService.getAllFilesByCar(carId, credentialId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOSS', 'USER')")
    @GetMapping("/filesByUser")
    public ResponseEntity<List<FileAndCarResponse>> getListFilesByCredentialId(@RequestParam("credentialId") Long credentialId){
        return ResponseEntity.ok(documentService.getAllFilesByUser(credentialId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
      return documentService.getFile(id, response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/zip-download", produces="application/zip")
    public byte[] zipFiles(@RequestParam("id") List<Long> fileId, HttpServletResponse response) throws IOException{
        return documentService.zippingFiles(fileId, response);
    }


}
