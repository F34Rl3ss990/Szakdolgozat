package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.web.data.DTO.FinanceDTO;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public interface DocumentService {

    Set<FileAndCarResponse> getAllFilesByUser(long credentialId);

    Set<FileAndCarResponse> getAllFilesByCar(long carId, long credentialId);

    void zippingFiles(List<Long> id, HttpServletResponse response) throws IOException;

    List<String> uploadService(MultipartFile[] multipartFiles) throws IOException;

    Document findDocument(long documentId);

    Resource download(long id);

    List<String> upload(MultipartFile[] multipartFiles, ServiceDataDTO serviceDataDTO,
                        FinanceDTO financeDTO, String email) throws IOException;

}
