package com.car_service.egea1r.web.data.payload.response;

import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
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
