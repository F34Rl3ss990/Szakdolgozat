package com.car_service.egea1r.web.data.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long fileId;
    private String documentType;
    private Date uploadTime;
    private String name;
    private String size;
    private String location;
    private Long carId;
    private Long serviceDataId;
    private String brand;
    private String carType;
    private String licensePlateNumber;
    private String mileage;
}
