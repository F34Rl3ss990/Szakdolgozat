package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.repository.CarRepository;
import com.EGEA1R.CarService.persistance.repository.ServiceRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.service.interfaces.ServiceService;
import com.EGEA1R.CarService.web.DTO.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;

    private UserRepository userRepository;

    private CarRepository carRepository;

    @Autowired
    public void setServiceRepository(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public void saveService(ServiceDTO serviceDTO){
        Car car = getCar(serviceDTO.getCarId());
        ServiceReservation serviceReservation = new ServiceReservation();

    }

    private Car getCar(Long carId){
        return carRepository
                .findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Car with this id not found: %s", Long.toString(carId))));
    }

}
