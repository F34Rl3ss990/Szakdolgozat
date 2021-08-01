package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.web.data.payload.request.DocumentRequest;
import org.springframework.core.io.FileSystemResource;

public interface FileSystemRepository {

    String saveServiceDocument(DocumentRequest document) throws Exception;

    FileSystemResource findInFileSystem(String location);

    String saveClientDocument(DocumentRequest document, String email) throws Exception;
}
