package com.car_service.egea1r.web.data.DTO;

import com.car_service.egea1r.validation.annotation.ValidLicensePlate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidLicensePlate(
        foreignPlate = "foreignCountryPlate",
        licensePlate = "licensePlateNumber"
)
public class CarDTO {

    private Long fkCarUserId;

    private Long carId;

    @NotNull
    private String brand;

    @NotNull
    private String type;

    @NotNull
    private String engineType;

    @NotNull
    private String yearOfManufacture;

    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String engineNumber;

    @Size(max = 17)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String chassisNumber;

    private String mileage;

    private String licensePlateNumber;

    @NotNull
    private Boolean foreignCountryPlate;

}
