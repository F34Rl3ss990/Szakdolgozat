package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.web.DTO.payload.response.FileAndCarResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DocumentService {



    FileSystemResource findDocument(Long documentId);

    List<FileAndCarResponse> getAllFilesByUser(Long credentialId);

    List<FileAndCarResponse> getAllFilesByCar(Long carId, Long credentialId);

    byte[] zippingFiles(List<Long> id, HttpServletResponse response) throws IOException;

    String storeServiceBigFiles(HttpServletRequest request);

    String storeClientBigFiles(HttpServletRequest request);

    ResponseEntity<byte[]> getFile(Long id, HttpServletResponse response) throws IOException;

}
