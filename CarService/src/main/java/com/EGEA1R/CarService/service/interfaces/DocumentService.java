package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.web.DTO.payload.response.FileAndCarResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    void storeServiceFiles(List<MultipartFile> fileToStore) throws Exception;

    void storeClientFiles(List<MultipartFile> fileToStore, Long fkCarId, String email, Long fkServiceDataId) throws Exception;

    FileSystemResource findDocument(Long documentId);

    List<FileAndCarResponse> getAllFilesByUser(Long credentialId);

    List<FileAndCarResponse> getAllFilesByCar(Long carId);

}
