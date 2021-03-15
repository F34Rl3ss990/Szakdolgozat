package com.EGEA1R.CarService.web.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @JsonProperty
    @NotNull
    private List<String> reservedServices = new ArrayList<>();
}
