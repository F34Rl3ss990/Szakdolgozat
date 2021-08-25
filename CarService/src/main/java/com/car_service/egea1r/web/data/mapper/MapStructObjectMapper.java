package com.car_service.egea1r.web.data.mapper;

import com.car_service.egea1r.persistance.entity.*;
import com.car_service.egea1r.web.data.DTO.*;
import com.car_service.egea1r.web.data.payload.request.ModifyUserDataRequest;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.web.data.payload.response.ResponseFile;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapStructObjectMapper {

    Car carDTOtoCar(CarDTO carDTO);

    CarMileage carDTOtoCarMileage(CarDTO carDTO);

    UserCarsDTO carToUserCarsDTO(Car car);

    ServiceReservation serviceReservationDTOtoServiceReservation(ServiceReservationDTO serviceReservationDTO);

    ServiceReservation unauthorizedUserReservationDTOtoServiceReservation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    Car unauthorizedUserReservationDTOtoCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    CarMileage unauthorizedUserReservationDTOtoCarMileage(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    BillingInformation modifyUserDataRequestToBillingInformation(ModifyUserDataRequest modifyUserDataRequest);

    User modifyUserDataRequestToUser(ModifyUserDataRequest modifyUserDataRequest);

    BillingInformation unauthorizedUserReservationDTOtoBillingInformation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    User unauthorizedUserReservationDTOtoUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    @Mapping(target="carId", source="serviceDataDTO.fkCarId")
    ServiceByUserResponse serviceDataDTOtoServiceByUserResponse(ServiceDataDTO serviceDataDTO);

    @Mapping(target="type", source="documentDTO.carType")
    FileAndCarResponse documentDTOtoFileAndCarResponse(DocumentDTO documentDTO);

    @Mapping(target = "type", source = "documentDTO.documentType")
    @Mapping(target = "id", source = "documentDTO.fileId")
    ResponseFile documentDTOtoResponseFile(DocumentDTO documentDTO);
}
