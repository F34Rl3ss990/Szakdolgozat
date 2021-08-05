package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DocumentService {



    FileSystemResource findDocument(long documentId);

    Set<FileAndCarResponse> getAllFilesByUser(long credentialId);

    Set<FileAndCarResponse> getAllFilesByCar(long carId, long credentialId);

    void zippingFiles(List<Long> id, HttpServletResponse response) throws IOException;

    String storeServiceBigFiles(HttpServletRequest request);

    void storeClientBigFiles(HttpServletRequest request) throws IOException, FileUploadException;

    ResponseEntity<byte[]> getFile(long id, HttpServletResponse response) throws IOException;

}
