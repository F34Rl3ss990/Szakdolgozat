package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.ServiceDataRepository;
import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDataServiceImplTest {

    ServiceDataServiceImpl serviceDataService;

    @Mock
    ServiceDataRepository serviceDataRepository;

    @Mock
    DocumentService documentService;

    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        serviceDataService = new ServiceDataServiceImpl(serviceDataRepository, documentService, mapStructObjectMapper);
    }



    @Test
    void saveDataAndFinance() {
    }

    @Test
    void getServiceDataListByUser() {
    }

    @Test
    void getServiceDataListByCar() {
    }
}
