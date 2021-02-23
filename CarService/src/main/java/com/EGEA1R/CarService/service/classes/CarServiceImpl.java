package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.CarMileage;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.interfaces.CarRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.UserRepository;
import com.EGEA1R.CarService.service.interfaces.CarService;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCar(CarDTO carDTO){
        if(checkLicensePlate(carDTO.getForeignCountryPlate(), carDTO.getLicensePlateNumber())){
            carRepository.addCar(mapCarDTOtoCar(carDTO));
        }
    }

    @Override
    public void modifyCar(CarDTO carDTO){
        if(checkLicensePlate(carDTO.getForeignCountryPlate(), carDTO.getLicensePlateNumber())) {
            carRepository.modifyCarById(mapCarDTOtoCar(carDTO));
        }
    }

    @Override
    public void deleteCar(Long id){
        carRepository.deleteCarById(id);
    }

    @Override
    public List<Car> getAllCarListByUser(Long userId){
        return carRepository.getAllCarByUser(userId);
    }

    @Override
    public Car getCarById(Long carId){
        return carRepository.finCarByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException( String.format("Car not found with id: %s", carId)));
    }

    public static Boolean checkLicensePlate(Boolean foreignPlate, String licensePlate){
        if(!foreignPlate && (Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", licensePlate)) ||
                Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", licensePlate)||
                Pattern.matches("^[/p/P][-][0-9]{5}$", licensePlate)||
                Pattern.matches("^[a-zA-z]{3}[0-9]{5}", licensePlate)){
            return true;
        }else if(foreignPlate){
            return true;
        } else{
            throw new BadRequestException("Code is incorrect");
        }
    }

    private Car mapCarDTOtoCar(CarDTO carDTO){
        Car car = modelMapper.map(carDTO, Car.class);
        CarMileage carMileage = modelMapper.map(carDTO, CarMileage.class);
        List<CarMileage> carMileages = new ArrayList<>();
        carMileages.add(carMileage);
        car.setCarMileages(carMileages);
        return car;
    }
}
