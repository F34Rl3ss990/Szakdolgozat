package com.EGEA1R.CarService.web.DTO.payload.response;

import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceByUserResponse {
    private Long carId;
    private String brand;
    private String type;
    private String licensePlateNumber;
    private List<ServiceDataDTO> serviceList;
}
