package com.EGEA1R.CarService.web.controller;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.web.DTO.payload.response.FileAndCarResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.ResponseFile;
import com.EGEA1R.CarService.web.DTO.payload.response.ResponseMessage;
import com.EGEA1R.CarService.service.interfaces.DocumentService;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/document")
public class DocumentController {

    private DocumentService documentService;

    @Autowired
    public void setDocumentService(DocumentService documentService){
        this.documentService = documentService;
    }


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") List<MultipartFile> files) {
        List<String> message = new ArrayList<>();
        try {
            documentService.storeServiceFiles(files);
            for(MultipartFile file : files){
                message.add("Uploaded the file successfully: " + file.getOriginalFilename());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            for(MultipartFile file : files) {
                message.add("Could not upload the file: " + file.getOriginalFilename() + "!");
            }
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    @GetMapping("/filesCarId")
    public ResponseEntity<List<FileAndCarResponse>> getListFilesByCarId(@RequestParam ("carId") Long carId) {
        List<FileAndCarResponse> files = documentService.getAllFilesByCar(carId);
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/filesByUser")
    public ResponseEntity<List<FileAndCarResponse>> getListFilesByCredentialId(@RequestParam ("credentialId") Long credentialId) {
        List<FileAndCarResponse> files = documentService.getAllFilesByUser(credentialId);
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public FileSystemResource getFile(@PathVariable Long id) {
        return documentService.findDocument(id);

    }

    @GetMapping(value = "/zip-download", produces="application/zip")
    public void zipDownload(@RequestBody List<Long> id, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (Long fileId : id) {
            FileSystemResource resource = documentService.findDocument(fileId);
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.getFile().length());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new Date().getTime() + "\"");
    }
}
