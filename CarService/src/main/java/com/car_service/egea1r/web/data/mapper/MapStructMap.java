package com.car_service.egea1r.web.data.mapper;

import com.car_service.egea1r.persistance.entity.*;
import com.car_service.egea1r.web.data.DTO.CarDTO;
import com.car_service.egea1r.web.data.DTO.ServiceReservationDTO;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import com.car_service.egea1r.web.data.DTO.UserCarsDTO;
import com.car_service.egea1r.web.data.payload.request.ModifyUserDateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MapStructMap {

    CarDTO carToCarDTO(Car car);

    Car carDTOToCar(CarDTO carDTO);

    CarMileage carDTOToCarMileage(CarDTO carDTO);

    UserCarsDTO carToUserCarsDTO(Car car);

    ServiceReservation serviceReservationDTOtoServiceReservation(ServiceReservationDTO serviceReservationDTO);

    ServiceReservation unauthorizedUserReservationDTOtoServiceReservation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    Car unauthorizedUserReservationDTOtoCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    CarMileage unauthorizedUserReservationDTOtoCarMileage(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    BillingInformation modifyUserDataRequestToBillingInformation(ModifyUserDateRequest modifyUserDateRequest);

    User modifyUserDataRequestToUser(ModifyUserDateRequest modifyUserDateRequest);

    BillingInformation unauthorizedUserReservationDTOtoBillingInformation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    User unauthorizedUserReservationDTOtoUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);
}
