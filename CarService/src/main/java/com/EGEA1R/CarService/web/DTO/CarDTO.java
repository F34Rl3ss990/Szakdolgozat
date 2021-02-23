package com.EGEA1R.CarService.web.DTO;

import com.EGEA1R.CarService.validation.annotation.ValidAccentAndWhitespace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private Long fkCarUserId;

    private Long carId;

    @NotNull
    @Size(max = 30)
    @ValidAccentAndWhitespace
    private String brand;

    @NotNull
    @Size(max = 30)
    private String type;

    @NotNull
    @Size(max = 30)
    private String engineType;

    @NotNull
    private String yearOfManufacture;

    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String engineNumber;

    @Size(max = 17)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String chassisNumber;

    @Max(9999999)
    @PositiveOrZero
    @NotNull
    private Integer mileage;

    @Size(max = 10)
    @NotNull
    private String licensePlateNumber;

    private Boolean foreignCountryPlate;
}
