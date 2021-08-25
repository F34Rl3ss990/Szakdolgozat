package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.ServiceDataRepository;
import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceDataServiceImplTest {

    @Mock
    ServiceDataRepository serviceDataRepository;

    @Mock
    DocumentService documentService;

    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @InjectMocks
    ServiceDataServiceImpl serviceDataService;

    @Test
    void getServiceDataListByUser() {
        long credentialId = 4L;
        List<ServiceDataDTO> serviceDataDTOList = new ArrayList<>();
        ServiceDataDTO serviceDataDTO = ServiceDataDTO.builder()
                .date(new Date())
                .amount("1000")
                .billNum("123456")
                .servicesDone("kerék csere")
                .brand("Audi")
                .type("A5")
                .licensePlateNumber("bbb-aaa")
                .comment("kész")
                .mileage("12345")
                .fkCarId(3L)
                .build();
        ServiceDataDTO serviceDataDTO2 = ServiceDataDTO.builder()
                .date(new Date())
                .amount("2000")
                .billNum("654321")
                .servicesDone("turbó csere")
                .comment("nincs kész")
                .brand("Audi")
                .type("A4")
                .licensePlateNumber("aaa-bbb")
                .mileage("54321")
                .fkCarId(5L)
                .build();
        ServiceDataDTO serviceDataDTO3 = ServiceDataDTO.builder()
                .date(new Date())
                .amount("2001")
                .billNum("3232")
                .servicesDone("kerék csere")
                .comment("kész")
                .brand("Audi")
                .type("A4")
                .licensePlateNumber("aaa-bbb")
                .mileage("543222")
                .fkCarId(5L)
                .build();
        serviceDataDTOList.add(serviceDataDTO);
        serviceDataDTOList.add(serviceDataDTO2);
        serviceDataDTOList.add(serviceDataDTO3);
        ServiceByUserResponse serviceByUserResponse = ServiceByUserResponse.builder()
                .brand("Audi")
                .type("A5")
                .licensePlateNumber("bbb-aaa")
                .carId(3L)
                .build();
        ServiceByUserResponse serviceByUserResponse2 = ServiceByUserResponse.builder()
                .brand("Audi")
                .type("A4")
                .licensePlateNumber("aaa-bbb")
                .carId(5L)
                .build();

        given(serviceDataRepository.getAllServiceByUser(credentialId)).willReturn(serviceDataDTOList);
        given(mapStructObjectMapper.serviceDataDTOtoServiceByUserResponse(serviceDataDTO)).willReturn(serviceByUserResponse);
        given(mapStructObjectMapper.serviceDataDTOtoServiceByUserResponse(serviceDataDTO2)).willReturn(serviceByUserResponse2);
        given(mapStructObjectMapper.serviceDataDTOtoServiceByUserResponse(serviceDataDTO3)).willReturn(serviceByUserResponse2);

        Set<ServiceByUserResponse> returnSet = serviceDataService.getServiceDataListByUser(credentialId);

        assertEquals(2, returnSet.size());
        verify(serviceDataRepository, times(1)).getAllServiceByUser(credentialId);
        verify(mapStructObjectMapper, times(1)).serviceDataDTOtoServiceByUserResponse(serviceDataDTO);
        verify(mapStructObjectMapper, times(1)).serviceDataDTOtoServiceByUserResponse(serviceDataDTO2);
        verify(mapStructObjectMapper, times(1)).serviceDataDTOtoServiceByUserResponse(serviceDataDTO3);
    }

    @Test
    void getServiceDataListByCar() {
        long carId = 3L;
        long credentialId = 5L;
        List<ServiceDataDTO> serviceDataDTOList = new ArrayList<>();
        ServiceDataDTO serviceDataDTO = ServiceDataDTO.builder()
                .date(new Date())
                .amount("1000")
                .billNum("123456")
                .servicesDone("kerék csere")
                .brand("Audi")
                .type("A5")
                .licensePlateNumber("bbb-aaa")
                .comment("kész")
                .mileage("12345")
                .fkCarId(3L)
                .build();
        ServiceDataDTO serviceDataDTO2 = ServiceDataDTO.builder()
                .date(new Date())
                .amount("2000")
                .billNum("654321")
                .servicesDone("turbó csere")
                .comment("nincs kész")
                .brand("Audi")
                .type("A4")
                .licensePlateNumber("aaa-bbb")
                .mileage("54321")
                .fkCarId(5L)
                .build();
        ServiceDataDTO serviceDataDTO3 = ServiceDataDTO.builder()
                .date(new Date())
                .amount("2001")
                .billNum("3232")
                .servicesDone("kerék csere")
                .comment("kész")
                .brand("Audi")
                .type("A4")
                .licensePlateNumber("aaa-bbb")
                .mileage("543222")
                .fkCarId(5L)
                .build();
        serviceDataDTOList.add(serviceDataDTO);
        serviceDataDTOList.add(serviceDataDTO2);
        serviceDataDTOList.add(serviceDataDTO3);
        given(serviceDataRepository.getAllServiceByCar(carId, credentialId)).willReturn(serviceDataDTOList);

        List<ServiceDataDTO> retList = serviceDataService.getServiceDataListByCar(carId, credentialId);

        assertEquals(retList, serviceDataDTOList);
        verify(serviceDataRepository, times(1)).getAllServiceByCar(carId, credentialId);
    }
}
