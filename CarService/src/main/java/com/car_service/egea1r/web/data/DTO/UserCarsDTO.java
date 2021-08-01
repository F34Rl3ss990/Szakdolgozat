package com.car_service.egea1r.web.data.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCarsDTO {

    private Long carId;
    private String brand;
    private String type;
    private String engineType;
    private String yearOfManufacture;
    private String engineNumber;
    private String chassisNumber;
    private String licensePlateNumber;
    private Long fkCarUser;
    private String mileage;
    private Date dateOfSet;
}
