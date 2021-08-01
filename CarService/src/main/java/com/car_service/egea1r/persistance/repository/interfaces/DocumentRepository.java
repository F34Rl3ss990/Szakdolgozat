package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.web.data.DTO.DocumentDTO;
import com.car_service.egea1r.web.data.payload.request.DocumentRequest;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    void saveServiceDocument(DocumentRequest document, String location);

    void saveClientDocument(DocumentRequest document, String location);

    Optional<Document> findById(long documentId);

    List<DocumentDTO> findAllByUser(long credentialId);

    List<DocumentDTO> findAllByCar(long carId, long credentialId);
}
