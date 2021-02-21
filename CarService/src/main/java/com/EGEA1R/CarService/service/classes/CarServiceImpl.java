package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.CarRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.service.interfaces.CarService;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    private UserRepository userRepository;

    private CredentialRepository credentialRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }

    @Override
    public void addCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, Long credentialId){
        Credential credential = credentialRepository
                .findById(credentialId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Credential not found %s", Long.toString(credentialId))));
        Car car = convertDTOtoCar(unauthorizedUserReservationDTO);
        User user = userRepository.findByCredential(credential);
        car.setUser(user);
        carRepository.save(car);
    }

    @Override
    public Car addCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, User user){
        Car car = convertDTOtoCar(unauthorizedUserReservationDTO);
        car.setUser(user);
        carRepository.save(car);
        return car;
    }

    @Override
    public void modifyCar(CarDTO carDTO){
        Car car = carRepository
                .findById(carDTO.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Car with id not found: %s", Long.toString(carDTO.getCarId()))));;
        car.setMileage(carDTO.getMileage());
        carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }

    @Override
    public Page<Car> getAllCarPage(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size,  Sort.by(Sort.Direction.ASC, "brand"));
        Page<Car> pageResult = carRepository.findAll(pageRequest);
        List<Car> cars = pageResult
                .stream()
                .collect(toList());
        return new PageImpl<>(cars, pageRequest, pageResult.getTotalElements());
    }

    @Override
    public Page<Car> getAllCarPageByUser(int page, int size, Long id){
        PageRequest pageRequest = PageRequest.of(page, size,  Sort.by(Sort.Direction.ASC, "brand"));
       // Page<Car> pageResult2 = carRepository.findAllByUser_UserId(id, pageRequest);
        Page<Car> pageResult = carRepository.findAll(pageRequest);
        Predicate<Car> contain = (Car item) -> item.getUser().getUserId() == id;
        List<Car> cars = pageResult
                .stream()
                .filter(contain)
                .collect(toList());
        return new PageImpl<>(cars, pageRequest, pageResult.getTotalElements());
    }
    @Override
    public List<Car> getAllCarListByUser(Long id){
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> getCarById(Long id){
        return carRepository.findById(id);
    }

    private Car convertDTOtoCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        Car car = new Car();
        String licensePlate = checkLicensePlate(unauthorizedUserReservationDTO.getForeignCountryPlate(), unauthorizedUserReservationDTO.getLicensePlateNumber());
        car.setLicensePlateNumber(licensePlate);
        car.setBrand(unauthorizedUserReservationDTO.getBrand());
        car.setType(unauthorizedUserReservationDTO.getType());
        car.setEngineType(unauthorizedUserReservationDTO.getEngineType());
        car.setYearOfManufacture(unauthorizedUserReservationDTO.getYearOfManufacture());
        car.setMileage(unauthorizedUserReservationDTO.getMileage());
        if(!unauthorizedUserReservationDTO.getChassisNumber().isEmpty()){
            car.setChassisNumber(unauthorizedUserReservationDTO.getChassisNumber());
        }
        if (!unauthorizedUserReservationDTO.getEngineNumber().isEmpty()){
            car.setEngineNumber(car.getEngineNumber());
        }
        return car;
    }


    private String checkLicensePlate(Boolean foreignPlate, String licensePlate){
        if(!foreignPlate && (Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", licensePlate)) ||
                Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", licensePlate)||
                Pattern.matches("^[/p/P][-][0-9]{5}$", licensePlate)||
                Pattern.matches("^[a-zA-z]{3}[0-9]{5}", licensePlate)){
            return licensePlate;
        }else if(foreignPlate){
            return licensePlate;
        } else{
            throw new BadRequestException("Code is incorrect");
        }
    }
}
