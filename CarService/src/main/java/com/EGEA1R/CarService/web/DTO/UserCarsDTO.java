package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.ValidAccentAndWhitespace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
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
