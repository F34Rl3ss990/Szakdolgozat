package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.web.data.mapper.MapStructMap;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.CarMileage;
import com.car_service.egea1r.persistance.repository.interfaces.CarRepository;
import com.car_service.egea1r.service.interfaces.CarService;
import com.car_service.egea1r.web.data.DTO.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final MapStructMap carMapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, UserRepository userRepository, MapStructMap carMapper) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
    }

    @Override
    public void addCar(CarDTO carDTO, long credentialId){
        mileageSetter(carDTO);
        long userId = userRepository.findUserIdByCredentialId(credentialId);
        carRepository.addCar(mapCarDTOtoCar(carDTO), carDTO.getMileage(), userId);
    }

    @Override
    public void modifyCar(CarDTO carDTO){
        mileageSetter(carDTO);
        carRepository.modifyCarById(mapCarDTOtoCar(carDTO));
    }

    @Override
    public List<Car> getAllCarListByUser(long userId){
        return carRepository.getAllCarByUser(userId);
    }

    @Override
    public Car getCarById(long carId){
        return carRepository.finCarByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("Car not found with id: %s", carId)));
    }

    private void mileageSetter(CarDTO carDTO){
        if(carDTO.getMileage() == null){
            carDTO.setMileage("");
        }
    }

    private Car mapCarDTOtoCar(CarDTO carDTO){
        Car car = carMapper.carDTOToCar(carDTO);
        List<CarMileage> carMileages = Collections.singletonList(carMapper.carDTOToCarMileage(carDTO));
        car.setCarMileages(carMileages);
        return car;
    }
}
