package com.car_service.egea1r.persistance.repository.classes;

import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.persistance.repository.interfaces.DocumentRepository;
import com.car_service.egea1r.web.data.DTO.DocumentDTO;
import com.car_service.egea1r.web.data.payload.request.DocumentRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class DocumentRepositoryImpl implements DocumentRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void saveServiceDocument(DocumentRequest document, String location) {
        em.createNativeQuery("insert into document(name, file_type, location, size) values(?, ?, ?, ?) ")
                .setParameter(1, document.getName())
                .setParameter(2, document.getFileType())
                .setParameter(3, location)
                .setParameter(4, document.getSize())
                .executeUpdate();
    }

    @Transactional
    @Override
    public void saveClientDocument(DocumentRequest document, String location) {
        em.createNativeQuery("insert into document(name, file_type, location, size, fk_document_car, fk_document_service_data) values(?, ?, ?, ?, ?, ?) ")
                .setParameter(1, document.getName())
                .setParameter(2, document.getFileType())
                .setParameter(3, location)
                .setParameter(4, document.getSize())
                .setParameter(5, document.getFkDocumentCarId())
                .setParameter(6, document.getFkDocumentServiceData())
                .executeUpdate();
    }

    @Override
    public Optional<Document> findById(long documentId) {
        try {
                StoredProcedureQuery query = em.createStoredProcedureQuery("DOWNLOAD_FILE_BY_ID", "GetDocumentForDownload");
                query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
                query.setParameter(1, documentId);
            return Optional.of((Document) query.getSingleResult());
        } catch (NoResultException n) {
            return Optional.empty();
        }
    }

    @Override
    public List<DocumentDTO> findAllByUser(long credentialId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_DOCUMENT_BY_USER_ORDER_BY_DATE", "GetDocumentsToShow");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, credentialId);
        return query.getResultList();
    }

    @Override
    public List<DocumentDTO> findAllByCar(long carId, long credentialId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_DOCUMENT_BY_CAR_ORDER_BY_DATE", "GetDocumentsToShow");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        query.setParameter(1, carId);
        query.setParameter(2, credentialId);
        return query.getResultList();
    }
}
