package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.web.DTO.ExcelCarDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelService {

    List<ExcelCarDTO> getExcelData();
}
