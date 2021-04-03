package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.web.DTO.payload.request.DocumentRequest;
import org.springframework.core.io.FileSystemResource;

public interface FileSystemRepository {

    String saveServiceDocument(DocumentRequest document) throws Exception;

    FileSystemResource findInFileSystem(String location);

    String saveClientDocument(DocumentRequest document, String email) throws Exception;
}
