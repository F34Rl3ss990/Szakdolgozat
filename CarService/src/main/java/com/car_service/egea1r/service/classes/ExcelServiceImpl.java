package com.car_service.egea1r.service.classes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.car_service.egea1r.service.interfaces.ExcelService;
import com.car_service.egea1r.web.data.DTO.ExcelCarDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Async
    public List<ExcelCarDTO> excelToCarDTO() throws IOException {
        String SHEET = "Munka1";
        FileInputStream fin = new FileInputStream("D:\\PROGRAMMING\\Szakdolgozat\\CarService\\src\\main\\resources\\serviceableCars.xlsx");
        try {
            Workbook workbook = new XSSFWorkbook(fin);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<ExcelCarDTO> carDTOs = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                ExcelCarDTO excelCarDTO = new ExcelCarDTO();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            if(currentCell.getCellType() == CellType.STRING) {
                                excelCarDTO.setBrand(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC){
                                excelCarDTO.setBrand(String.valueOf(currentCell.getNumericCellValue()));
                            }
                            break;
                        case 1:
                            if(currentCell.getCellType() == CellType.STRING) {
                                excelCarDTO.setType(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC){
                                String numericType = String.valueOf(currentCell.getNumericCellValue());
                                numericType  = numericType.substring(0, numericType.length() - 2);
                                excelCarDTO.setType(numericType);
                            }
                            break;
                        case 2:
                            if(currentCell.getCellType() == CellType.STRING) {
                                excelCarDTO.setYearOfManufacture(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC){
                                excelCarDTO.setYearOfManufacture((String.valueOf(currentCell.getNumericCellValue())).substring(0,4));
                            }
                            break;
                        case 3:
                            if(currentCell.getCellType() == CellType.STRING) {
                                excelCarDTO.setEngineType(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC){
                                excelCarDTO.setEngineType(String.valueOf(currentCell.getNumericCellValue()));
                            }
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                carDTOs.add(excelCarDTO);
            }
            workbook.close();
            fin.close();
            return carDTOs;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public List<ExcelCarDTO> getExcelData() {
        try {
            return excelToCarDTO();
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}
