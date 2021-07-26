package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.interfaces.UserRepository;
import com.EGEA1R.CarService.web.exception.BadRequestException;
import com.EGEA1R.CarService.web.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.CarMileage;
import com.EGEA1R.CarService.persistance.repository.interfaces.CarRepository;
import com.EGEA1R.CarService.service.interfaces.CarService;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    private ModelMapper modelMapper;

    private UserRepository userRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void addCar(CarDTO carDTO, Long credentialId){
        mileageSetter(carDTO);
        Long userId = userRepository.findUserIdByCredentialId(credentialId);
        carRepository.addCar(mapCarDTOtoCar(carDTO), carDTO.getMileage(), userId);
    }

    @Override
    public void modifyCar(CarDTO carDTO){
        mileageSetter(carDTO);
        carRepository.modifyCarById(mapCarDTOtoCar(carDTO));
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

    private void mileageSetter(CarDTO carDTO){
        String mileageNum = (carDTO.getMileage() == null) ? "" : carDTO.getMileage();
        if (mileageNum.equals("")) {
            carDTO.setMileage(mileageNum);
        }
    }
/*
    public static Boolean checkLicensePlate(Boolean foreignPlate, String licensePlate){
        if(!foreignPlate && (Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", licensePlate)) ||
                Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", licensePlate)||
                Pattern.matches("^[/p/P][-][0-9]{5}$", licensePlate)||
                Pattern.matches("^[a-zA-z]{3}[0-9]{5}", licensePlate)){
            return true;
        }else if(foreignPlate){
            return true;
        } else{
            throw new BadRequestException(String.format("LicensePlate is incorrect: %s", licensePlate));
        }
    }*/

    private Car mapCarDTOtoCar(CarDTO carDTO){
        Car car = modelMapper.map(carDTO, Car.class);
        CarMileage carMileage = modelMapper.map(carDTO, CarMileage.class);
        List<CarMileage> carMileages = new ArrayList<>();
        carMileages.add(carMileage);
        car.setCarMileages(carMileages);
        return car;
    }
}
