package com.car_service.egea1r.persistance.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.awt.*;
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
