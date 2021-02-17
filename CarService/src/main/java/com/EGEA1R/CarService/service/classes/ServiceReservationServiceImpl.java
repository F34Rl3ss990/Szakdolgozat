package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.CarRepository;
import com.EGEA1R.CarService.persistance.repository.ServiceReservationRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.service.interfaces.ServiceReservationService;
import com.EGEA1R.CarService.web.DTO.ReservedServiceList;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
public class ServiceReservationServiceImpl implements ServiceReservationService {

    private ServiceReservationRepository serviceReservationRepository;

    private UserRepository userRepository;

    private CarRepository carRepository;

    @Autowired
    public void setServiceRepository(ServiceReservationRepository serviceReservationRepository){
        this.serviceReservationRepository = serviceReservationRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public void saveService(ServiceReservationDTO serviceReservationDTO){
        Car car = getCar(serviceReservationDTO.getCarId());
        List<String> services = new ArrayList<>();
        ServiceReservation serviceReservation = new ServiceReservation();
        serviceReservation.setReservedDate(serviceReservationDTO.getReservedDate());
        if(!serviceReservationDTO.getComment().isEmpty()) {
            serviceReservation.setComment(serviceReservationDTO.getComment());
        }
        for(ReservedServiceList item : serviceReservationDTO.getService()){
            if(item.getChecked()) {
                services.add(item.getServiceType());
            }
        }
        serviceReservation.setServices(services);
        serviceReservation.setCar(car);
        serviceReservationRepository.save(serviceReservation);
    }

    public void saveService(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, Car car){
        List<String> services = new ArrayList<>();
        ServiceReservation serviceReservation = new ServiceReservation();
        serviceReservation.setReservedDate(unauthorizedUserReservationDTO.getReservedDate());
        if(!unauthorizedUserReservationDTO.getComment().isEmpty()) {
            serviceReservation.setComment(unauthorizedUserReservationDTO.getComment());
        }
        for(ReservedServiceList item : unauthorizedUserReservationDTO.getService()){
            if(item.getChecked()) {
                services.add(item.getServiceType());
            }
        }
        serviceReservation.setServices(services);
        serviceReservation.setCar(car);
        serviceReservationRepository.save(serviceReservation);
    }

    public Page<ServiceReservation> getServicesByUserOrderByDate(int page, int size, Long id){
        PageRequest pageRequest = PageRequest.of(page, size,  Sort.by(Sort.Direction.ASC, "reservedDate"));
        Page<ServiceReservation> pageResult = serviceReservationRepository.findAll(pageRequest);
        Predicate<ServiceReservation> contain = (ServiceReservation item) -> item.getCar().getUser().getUserId() == id;
        List<ServiceReservation> serviceReservations = pageResult
                .stream()
                .filter(contain)
                .collect(toList());
        return new PageImpl<>(serviceReservations, pageRequest, pageResult.getTotalElements());
    }

    public Page<ServiceReservation> getServicesTodayOrderByDate(int page, int size){
        LocalDate localDate = LocalDate.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        PageRequest pageRequest = PageRequest.of(page, size,  Sort.by(Sort.Direction.ASC, "reservedDate"));
        Page<ServiceReservation> pageResult = serviceReservationRepository.findAllByReservedDate(localDate, pageRequest);
        List<ServiceReservation> serviceReservations = pageResult
                .stream()
                .collect(toList());
        return new PageImpl<>(serviceReservations, pageRequest, pageResult.getTotalElements());
    }


    private Car getCar(Long carId){
        return carRepository
                .findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Car with this id not found: %s", Long.toString(carId))));
    }

}
