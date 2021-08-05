package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.*;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceImplTest {

    DocumentServiceImpl documentService;

    @Mock
    DocumentRepository documentRepository;
    @Mock
    FileSystemRepository fileSystemRepository;
    @Mock
    ServiceReservationRepository serviceReservationRepository;
    @Mock
    FinanceRepository financeRepository;
    @Mock
    ServiceDataRepository serviceDataRepository;
    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        documentService = new DocumentServiceImpl(documentRepository, fileSystemRepository, serviceReservationRepository,
                financeRepository, serviceDataRepository, mapStructObjectMapper);
    }

    @Test
    void storeServiceBigFiles() {
    }

    @Test
    void storeClientBigFiles() {
    }

    @Test
    void findDocument() {
    }

    @Test
    void getAllFilesByUser() {
    }

    @Test
    void getAllFilesByCar() {
    }

    @Test
    void zippingFiles() {
    }

    @Test
    void getFile() {
    }
}
