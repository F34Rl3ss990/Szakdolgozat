package com.car_service.egea1r.web.data.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservationDTO {

    @NotNull
    private Long fkServiceReservationCarId;

    private String comment;

    @NotNull
    @FutureOrPresent
    private Date reservedDate;

    @NotNull
    private String reservedServices;

    private Long carId;
}
