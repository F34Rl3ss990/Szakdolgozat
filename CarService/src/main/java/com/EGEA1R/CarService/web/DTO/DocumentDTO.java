package com.EGEA1R.CarService.web.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
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

    public DocumentDTO(Long fileId, String documentType, Date uploadTime, String name, String size, String location, Long carId, Long serviceDataId, String brand, String carType, String licensePlateNumber, String mileage){
        this.fileId = fileId;
        this.documentType = documentType;
        this.uploadTime = uploadTime;
        this.name = name;
        this.size = size;
        this.location = location;
        this.carId = carId;
        this.brand = brand;
        this.carType = carType;
        this.serviceDataId = serviceDataId;
        this.licensePlateNumber = licensePlateNumber;
        this.mileage = mileage;
    }
}
