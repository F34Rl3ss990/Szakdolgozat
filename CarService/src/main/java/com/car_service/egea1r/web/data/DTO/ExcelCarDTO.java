package com.car_service.egea1r.web.data.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCarDTO {

    private String brand;
    private String type;
    private String yearOfManufacture;
    private String engineType;
}
