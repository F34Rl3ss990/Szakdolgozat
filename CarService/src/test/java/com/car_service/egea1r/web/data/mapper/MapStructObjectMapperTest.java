package com.car_service.egea1r.web.data.mapper;

import com.car_service.egea1r.persistance.entity.*;
import com.car_service.egea1r.web.data.DTO.*;
import com.car_service.egea1r.web.data.payload.request.ModifyUserDataRequest;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.web.data.payload.response.ResponseFile;
import com.car_service.egea1r.web.data.payload.response.ServiceByUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MapStructObjectMapperTest {

    MapStructObjectMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new MapStructObjectMapperImpl();
    }

    @Test
    void givenCarDTOtoCar_whenMaps_thenCorrect() {
        CarDTO carDTO = CarDTO.builder()
                .carId(5L)
                .brand("Audi")
                .type("A4")
                .engineType("1.9 TDI")
                .engineNumber("asdqwe123")
                .fkCarUserId(4L)
                .yearOfManufacture("2000")
                .chassisNumber("asdqwe123asdqwe12")
                .licensePlateNumber("xyz-123")
                .build();

        Car car = mapper.carDTOtoCar(carDTO);

        assertEquals(carDTO.getCarId(), car.getCarId());
        assertEquals(carDTO.getBrand(), car.getBrand());
        assertEquals(carDTO.getType(), car.getType());
        assertEquals(carDTO.getEngineType(), car.getEngineType());
        assertEquals(carDTO.getEngineNumber(), car.getEngineNumber());
        assertEquals(carDTO.getFkCarUserId(), car.getFkCarUserId());
        assertEquals(carDTO.getYearOfManufacture(), car.getYearOfManufacture());
        assertEquals(carDTO.getChassisNumber(), car.getChassisNumber());
        assertEquals(carDTO.getLicensePlateNumber(), car.getLicensePlateNumber());
    }

    @Test
    void givenCarDTOtoCarMileage_whenMaps_thenCorrect() {
        CarDTO carDTO = CarDTO.builder()
                .mileage("123456")
                .build();

        CarMileage carMileage = mapper.carDTOtoCarMileage(carDTO);

        assertEquals(carDTO.getMileage(), carMileage.getMileage());
    }

    @Test
    void givenCarToUserCarsDTO_whenMaps_thenCorrect() {

        Car car = Car.builder()
                .carId(5L)
                .brand("Audi")
                .type("A4")
                .engineType("1.9 TDI")
                .engineNumber("asdqwe123")
                .yearOfManufacture("2000")
                .chassisNumber("asdqwe123asdqwe12")
                .licensePlateNumber("xyz-123")
                .build();

        UserCarsDTO userCarsDTO = mapper.carToUserCarsDTO(car);

        assertEquals(car.getCarId(), userCarsDTO.getCarId());
        assertEquals(car.getBrand(), userCarsDTO.getBrand());
        assertEquals(car.getType(), userCarsDTO.getType());
        assertEquals(car.getEngineType(), userCarsDTO.getEngineType());
        assertEquals(car.getEngineNumber(), userCarsDTO.getEngineNumber());
        assertEquals(car.getYearOfManufacture(), userCarsDTO.getYearOfManufacture());
        assertEquals(car.getChassisNumber(), userCarsDTO.getChassisNumber());
        assertEquals(car.getLicensePlateNumber(), userCarsDTO.getLicensePlateNumber());
    }

    @Test
    void givenServiceReservationDTOtoServiceReservation_whenMaps_thenCorrect() {
        ServiceReservationDTO serviceReservationDTO = ServiceReservationDTO.builder()
                .fkServiceReservationCarId(5L)
                .comment("123456789asdfghjkl")
                .reservedServices("Tyre, Brake")
                .build();

        ServiceReservation serviceReservation = mapper.serviceReservationDTOtoServiceReservation(serviceReservationDTO);

        assertEquals(serviceReservationDTO.getFkServiceReservationCarId(), serviceReservation.getFkServiceReservationCarId());
        assertEquals(serviceReservationDTO.getReservedServices(), serviceReservation.getReservedServices());
        assertEquals(serviceReservationDTO.getComment(), serviceReservation.getComment());
    }

    @Test
    void givenUnauthorizedUserReservationDTOtoServiceReservation_whenMaps_thenCorrect() {
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .comment("123456789asdfghjkl")
                .reservedDate(new Date())
                .reservedServices("Tyre, Brake")
                .build();

        ServiceReservation serviceReservation = mapper.unauthorizedUserReservationDTOtoServiceReservation(unauthorizedUserReservationDTO);

        assertEquals(unauthorizedUserReservationDTO.getComment(), serviceReservation.getComment());
        assertEquals(unauthorizedUserReservationDTO.getReservedDate(), serviceReservation.getReservedDate());
        assertEquals(unauthorizedUserReservationDTO.getReservedServices(), serviceReservation.getReservedServices());
    }

    @Test
    void givenUnauthorizedUserReservationDTOtoCar_whenMaps_thenCorrect() {
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .brand("Audi")
                .type("A4")
                .engineType("1.9 TDI")
                .engineNumber("asdqwe123")
                .yearOfManufacture("2000")
                .chassisNumber("asdqwe123asdqwe12")
                .licensePlateNumber("xyz-123")
                .build();

        Car car = mapper.unauthorizedUserReservationDTOtoCar(unauthorizedUserReservationDTO);

        assertEquals(unauthorizedUserReservationDTO.getBrand(), car.getBrand());
        assertEquals(unauthorizedUserReservationDTO.getType(), car.getType());
        assertEquals(unauthorizedUserReservationDTO.getEngineType(), car.getEngineType());
        assertEquals(unauthorizedUserReservationDTO.getEngineNumber(), car.getEngineNumber());
        assertEquals(unauthorizedUserReservationDTO.getYearOfManufacture(), car.getYearOfManufacture());
        assertEquals(unauthorizedUserReservationDTO.getChassisNumber(), car.getChassisNumber());
        assertEquals(unauthorizedUserReservationDTO.getLicensePlateNumber(), car.getLicensePlateNumber());
    }

    @Test
    void givenUnauthorizedUserReservationDTOtoCarMileage_whenMaps_thenCorrect() {
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .mileage("123456")
                .build();

        CarMileage carMileage = mapper.unauthorizedUserReservationDTOtoCarMileage(unauthorizedUserReservationDTO);

        assertEquals(unauthorizedUserReservationDTO.getMileage(), carMileage.getMileage());
    }

    @Test
    void givenModifyUserDataRequestToBillingInformation_whenMaps_thenCorrect() {
        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .billingName("Katona József")
                .billingPhoneNumber("06301234567")
                .billingZipCode(1234)
                .billingStreet("Asd út 2")
                .billingOtherAddressType("2. emelet 3. ajtó")
                .billingTaxNumber("12345678-1-12")
                .billingEmail("valami@gmail.com")
                .build();

        BillingInformation billingInformation = mapper.modifyUserDataRequestToBillingInformation(modifyUserDataRequest);

        assertEquals(modifyUserDataRequest.getBillingName(), billingInformation.getBillingName());
        assertEquals(modifyUserDataRequest.getBillingPhoneNumber(), billingInformation.getBillingPhoneNumber());
        assertEquals(modifyUserDataRequest.getBillingZipCode(), billingInformation.getBillingZipCode());
        assertEquals(modifyUserDataRequest.getBillingStreet(), billingInformation.getBillingStreet());
        assertEquals(modifyUserDataRequest.getBillingOtherAddressType(), billingInformation.getBillingOtherAddressType());
        assertEquals(modifyUserDataRequest.getBillingTaxNumber(), billingInformation.getBillingTaxNumber());
        assertEquals(modifyUserDataRequest.getBillingEmail(), billingInformation.getBillingEmail());
    }

    @Test
    void givenModifyUserDataRequestToUser_whenMaps_thenCorrect() {
        ModifyUserDataRequest modifyUserDataRequest = ModifyUserDataRequest.builder()
                .userId(3L)
                .build();

        User user = mapper.modifyUserDataRequestToUser(modifyUserDataRequest);

        assertEquals(modifyUserDataRequest.getUserId(), user.getUserId());
    }

    @Test
    void givenUnauthorizedUserReservationDTOtoBillingInformation_whenMaps_thenCorrect() {
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .billingName("Katona József")
                .billingPhoneNumber("06301234567")
                .billingZipCode(1234)
                .billingStreet("Asd út 2")
                .billingOtherAddressType("2. emelet 3. ajtó")
                .billingTaxNumber("12345678-1-12")
                .billingEmail("valami@gmail.com")
                .build();

        BillingInformation billingInformation = mapper.unauthorizedUserReservationDTOtoBillingInformation(unauthorizedUserReservationDTO);

        assertEquals(unauthorizedUserReservationDTO.getBillingName(), billingInformation.getBillingName());
        assertEquals(unauthorizedUserReservationDTO.getBillingPhoneNumber(), billingInformation.getBillingPhoneNumber());
        assertEquals(unauthorizedUserReservationDTO.getBillingZipCode(), billingInformation.getBillingZipCode());
        assertEquals(unauthorizedUserReservationDTO.getBillingStreet(), billingInformation.getBillingStreet());
        assertEquals(unauthorizedUserReservationDTO.getBillingOtherAddressType(), billingInformation.getBillingOtherAddressType());
        assertEquals(unauthorizedUserReservationDTO.getBillingTaxNumber(), billingInformation.getBillingTaxNumber());
        assertEquals(unauthorizedUserReservationDTO.getBillingEmail(), billingInformation.getBillingEmail());
    }

    @Test
    void givenUnauthorizedUserReservationDTOtoUser_whenMaps_thenCorrect() {
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .name("Katona József")
                .email("asd@gmail.com")
                .phoneNumber("06301234567")
                .build();

        User user = mapper.unauthorizedUserReservationDTOtoUser(unauthorizedUserReservationDTO);

        assertEquals(unauthorizedUserReservationDTO.getName(), user.getName());
        assertEquals(unauthorizedUserReservationDTO.getEmail(), user.getEmail());
        assertEquals(unauthorizedUserReservationDTO.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    void givenServiceDataDTOtoServiceByUserResponse_whenMaps_thenCorrect() {
        ServiceDataDTO serviceDataDTO = ServiceDataDTO.builder()
                .brand("Audi")
                .type("A4")
                .licensePlateNumber("asd-123")
                .build();

        ServiceByUserResponse serviceByUserResponse = mapper.serviceDataDTOtoServiceByUserResponse(serviceDataDTO);

        assertEquals(serviceDataDTO.getBrand(), serviceByUserResponse.getBrand());
        assertEquals(serviceDataDTO.getType(), serviceByUserResponse.getType());
        assertEquals(serviceDataDTO.getLicensePlateNumber(), serviceByUserResponse.getLicensePlateNumber());
    }

    @Test
    void givenDocumentDTOtoFileAndCarResponse_whenMaps_thenCorrect() {
        DocumentDTO documentDTO = DocumentDTO.builder()
                .carId(5L)
                .serviceDataId(4L)
                .brand("Audi")
                .carType("A4")
                .licensePlateNumber("asd-123")
                .mileage("123456")
                .build();

        FileAndCarResponse fileAndCarResponse = mapper.documentDTOtoFileAndCarResponse(documentDTO);

        assertEquals(documentDTO.getCarId(), fileAndCarResponse.getCarId());
        assertEquals(documentDTO.getServiceDataId(), fileAndCarResponse.getServiceDataId());
        assertEquals(documentDTO.getBrand(), fileAndCarResponse.getBrand());
        assertEquals(documentDTO.getCarType(), fileAndCarResponse.getType());
        assertEquals(documentDTO.getLicensePlateNumber(), fileAndCarResponse.getLicensePlateNumber());
        assertEquals(documentDTO.getMileage(), fileAndCarResponse.getMileage());
    }

    @Test
    void givenDocumentDTOtoResponseFile_whenMaps_thenCorrect() {
        DocumentDTO documentDTO = DocumentDTO.builder()
                .name("file23")
                .documentType("xml")
                .fileId(5L)
                .size("1234")
                .build();

        ResponseFile responseFile = mapper.documentDTOtoResponseFile(documentDTO);

        assertEquals(documentDTO.getName(), responseFile.getName());
        assertEquals(documentDTO.getDocumentType(), responseFile.getType());
        assertEquals(documentDTO.getFileId(), responseFile.getId());
        assertEquals(documentDTO.getSize(), responseFile.getSize());
    }
}
