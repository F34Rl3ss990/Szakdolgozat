package com.EGEA1R.CarService.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {

    @NotNull
    private Long carId;

    private String comment;

    @CreationTimestamp
    private Date serviceDate;

    @NotNull
    @FutureOrPresent
    private Date reservedDate;

    private List<ServiceList> service = new ArrayList<>();
}
