package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.web.DTO.DocumentDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.DocumentRequest;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    void saveServiceDocument(DocumentRequest document, String location);

    void saveClientDocument(DocumentRequest document, String location);

    Optional<Document> findById(Long documentId);

    List<DocumentDTO> findAllByUser(Long credentialId);

    List<DocumentDTO> findAllByCar(Long carId);
}
