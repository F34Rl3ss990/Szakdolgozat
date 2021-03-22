package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.CarMileage;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.interfaces.ServiceReservationRepository;
import com.EGEA1R.CarService.service.interfaces.CarService;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.ServiceReservationService;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.UserCarsDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ServiceReservationServiceImpl implements ServiceReservationService {

    private ServiceReservationRepository serviceReservationRepository;

    private CarService carService;

    private UserService userService;

    private ModelMapper modelMapper;

    private EmailService emailService;

    @Autowired
    public void setServiceRepository(ServiceReservationRepository serviceReservationRepository){
        this.serviceReservationRepository = serviceReservationRepository;
    }

    @Autowired
    public void setCarService(CarService carService){
        this.carService = carService;
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void saveService(ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException {
        serviceReservationRepository.saveService(mapDTOtoServiceReservation(serviceReservationDTO));
        sendEmailAfterServiceReservation(serviceReservationDTO);
    }

    @Override
    public PagedListHolder<ServiceReservation> getServicesByUserOrderByDate(int page, int size, Long userId){
        List<ServiceReservation> services = serviceReservationRepository.getAllServiceByUser(userId);
        PagedListHolder<ServiceReservation> pageHolder = new PagedListHolder<>(services);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }

    @Override
    public PagedListHolder<ServiceReservation> getServicesTodayOrderByDate(int page, int size){
        LocalDate localDate = LocalDate.now();
        List<ServiceReservation> services = serviceReservationRepository.getAllServiceByTodayDate(localDate);
        PagedListHolder<ServiceReservation> pageHolder = new PagedListHolder<>(services);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }

    @Override
    public List<UserCarsDTO> getCarByCredentialId(Long credentialId) throws ParseException {
        List<UserCarsDTO> userCars = new ArrayList<>();
        String maxMileage = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.");
        Date date;
        date = sdf.parse("2000.01.01.");
        for (Car car : serviceReservationRepository.getAllCarByCredentialId(credentialId)) {
            for (CarMileage mileage : car.getCarMileages()) {
                if (mileage.getDateOfSet().compareTo(date) > 0) {
                    date = mileage.getDateOfSet();
                    maxMileage = mileage.getMileage();
                }
            }
            userCars.add(mapDTOtoUserCarsDTO(car, date, maxMileage));
        }
        return userCars;
    }

    private UserCarsDTO mapDTOtoUserCarsDTO(Car car, Date date, String maxMilege){
        UserCarsDTO userCarsDTO = modelMapper.map(car, UserCarsDTO.class);
        userCarsDTO.setMileage(maxMilege);
        userCarsDTO.setDateOfSet(date);
        return userCarsDTO;
    }

    private ServiceReservation mapDTOtoServiceReservation(ServiceReservationDTO serviceReservationDTO){
         return modelMapper.map(serviceReservationDTO, ServiceReservation.class);
    }

    private void sendEmailAfterServiceReservation(ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException {
        Car car = carService.getCarById(serviceReservationDTO.getFkServiceReservationCarId());
        User user = userService.getUserDetailsByCarId(serviceReservationDTO.getFkServiceReservationCarId());
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .brand(car.getBrand())
                .type(car.getType())
                .engineType(car.getEngineType())
                .yearOfManufacture(car.getYearOfManufacture())
                .engineNumber(car.getEngineNumber())
                .chassisNumber(car.getChassisNumber())
                .mileage(car.getCarMileages().get(0).getMileage())
                .licensePlateNumber(car.getLicensePlateNumber())
                .reservedDate(serviceReservationDTO.getReservedDate())
                .reservedServices(serviceReservationDTO.getReservedServices())
                .comment(serviceReservationDTO.getComment())
                .build();
        emailService.sendReservedServiceInformation(unauthorizedUserReservationDTO);
    }

}
