package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.web.data.DTO.ExcelCarDTO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelServiceImplTest {

    @InjectMocks
    ExcelServiceImpl excelService;

    @Test
    void excelToCarDTO() throws IOException, ExecutionException, InterruptedException {
       CompletableFuture<List<ExcelCarDTO>> list = excelService.excelToCarDTO();
       assertTrue(list.get().size()>0);
    }
}
