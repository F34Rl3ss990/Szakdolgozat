package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.CarMileage;
import com.car_service.egea1r.persistance.repository.interfaces.CarRepository;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.web.data.DTO.CarDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @InjectMocks
    CarServiceImpl carService;

    CarDTO carDTO;

    Car car;

    @BeforeEach
    void setUp() {
        carDTO = CarDTO.builder()
                .brand("Audi")
                .type("A4")
                .build();

        car = Car.builder()
                .brand("Audi")
                .type("A4")
                .chassisNumber("asdqwe123asdqwe12")
                .licensePlateNumber("xyz-123")
                .yearOfManufacture("2000")
                .engineNumber("asdqwe123")
                .engineType("1.9 TDI")
                .build();
    }

    @Test
    void addCar_whenValid_thenCorrect() {
        long id = 5;
        long id2 = 2;

        CarMileage carMileage = CarMileage.builder()
                .mileage("")
                .build();
        car.setCarMileages(Collections.singletonList(carMileage));

        given(userRepository.findUserIdByCredentialId(id)).willReturn(id2);
        given(mapStructObjectMapper.carDTOtoCar(carDTO)).willReturn(car);

        carService.addCar(carDTO, id);

        verify(carRepository, times(1)).addCar(car, carMileage.getMileage(), id2);
        verify(userRepository, times(1)).findUserIdByCredentialId(id);
        verify(mapStructObjectMapper, times(1)).carDTOtoCar(carDTO);
    }

    @Test
    void modifyCar_whenValid_thenCorrect() {
        given(mapStructObjectMapper.carDTOtoCar(carDTO)).willReturn(car);

        carService.modifyCar(carDTO);

        verify(carRepository, times(1)).modifyCarById(car);
        verify(mapStructObjectMapper, times(1)).carDTOtoCar(carDTO);
    }

    @Test
    void getAllCarListByUser_whenValid_thenCorrect() {
        long id = 3L;
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        given(carRepository.getAllCarByUser(id)).willReturn(carList);

        List<Car> carListFromService = carService.getAllCarListByUser(id);

        assertEquals(carListFromService.size(), carList.size());
        assertEquals(carListFromService.get(0), carList.get(0));
        verify(carRepository, times(1)).getAllCarByUser(id);
    }

    @Test
    void getCarById_whenValid_thenCorrect() {
        long id = 3L;
        given(carRepository.finCarByCarId(id)).willReturn(Optional.of(car));

        Car car2 = carService.getCarById(id);

        assertEquals(car, car2);
        verify(carRepository, times(1)).finCarByCarId(3L);
    }

    @Test
    void getCarById_whenNotFound_thenExceptionThrown() {
        long id = 3L;
        given(carRepository.finCarByCarId(id)).willReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> carService.getCarById(id));

        String expectedMessage = "Car not found with id: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(carRepository, times(1)).finCarByCarId(3L);
    }
}
