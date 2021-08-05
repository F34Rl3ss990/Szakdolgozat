package com.car_service.egea1r.service.classes;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ExcelServiceImplTest {

    ExcelServiceImpl excelService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        excelService = new ExcelServiceImpl();
    }

    @Test
    void excelToCarDTO() {
    }
}
