package com.EGEA1R.CarService.web.controller;

import java.io.*;
import java.util.List;
import com.EGEA1R.CarService.web.DTO.payload.response.FileAndCarResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import com.EGEA1R.CarService.service.interfaces.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/document")
public class DocumentController {

    private DocumentService documentService;

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }


    @PostMapping(value="/uploadBigFile")
    public MessageResponse upload(HttpServletRequest request) {
        String message = documentService.storeServiceBigFiles(request);
        return new MessageResponse(message);
    }

    @GetMapping("/filesCarId")
    public ResponseEntity<List<FileAndCarResponse>> getListFilesByCarId(@RequestParam("carId") Long carId, @RequestParam("credentialId") Long credentialId){
        List<FileAndCarResponse> files = documentService.getAllFilesByCar(carId, credentialId);
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/filesByUser")
    public ResponseEntity<List<FileAndCarResponse>> getListFilesByCredentialId(@RequestParam("credentialId") Long credentialId){
        List<FileAndCarResponse> files = documentService.getAllFilesByUser(credentialId);
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
      return documentService.getFile(id, response);
    }

    @GetMapping(value = "/zip-download", produces="application/zip")
    public byte[] zipFiles(@RequestParam("id") List<Long> fileId, HttpServletResponse response) throws IOException{
        return documentService.zippingFiles(fileId, response);
    }


}
