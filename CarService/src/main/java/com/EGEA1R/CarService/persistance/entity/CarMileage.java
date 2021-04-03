package com.EGEA1R.CarService.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CarMileage {

    @NotNull
    private String mileage;

    private Date dateOfSet;
}
