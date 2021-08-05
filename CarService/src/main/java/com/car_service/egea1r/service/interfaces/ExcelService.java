package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.web.data.DTO.ExcelCarDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ExcelService {

    CompletableFuture<List<ExcelCarDTO>> excelToCarDTO() throws IOException;
}
