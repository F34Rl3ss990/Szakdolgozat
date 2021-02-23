package com.EGEA1R.CarService.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CarMileage {

    @NotNull
    @Max(9999999)
    @PositiveOrZero
    private Integer mileage;

    private Date dateOfSet;
}
