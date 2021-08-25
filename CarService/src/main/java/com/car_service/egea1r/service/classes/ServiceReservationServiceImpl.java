package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.CarMileage;
import com.car_service.egea1r.persistance.entity.ServiceReservation;
import com.car_service.egea1r.persistance.repository.interfaces.ServiceReservationRepository;
import com.car_service.egea1r.service.interfaces.CarService;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.ServiceReservationService;
import com.car_service.egea1r.service.interfaces.UserService;
import com.car_service.egea1r.web.data.DTO.ServiceReservationDTO;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import com.car_service.egea1r.web.data.DTO.UserCarsDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
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

    private final ServiceReservationRepository serviceReservationRepository;
    private final CarService carService;
    private final UserService userService;
    private final EmailService emailService;
    private final MapStructObjectMapper mapStructObjectMapper;

    @Autowired
    public ServiceReservationServiceImpl(ServiceReservationRepository serviceReservationRepository, CarService carService, UserService userService, EmailService emailService, MapStructObjectMapper mapStructObjectMapper) {
        this.serviceReservationRepository = serviceReservationRepository;
        this.carService = carService;
        this.userService = userService;
        this.emailService = emailService;
        this.mapStructObjectMapper = mapStructObjectMapper;
    }

    @Transactional
    @Override
    public void saveService(ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException {
        serviceReservationRepository.saveService(mapDTOtoServiceReservation(serviceReservationDTO));
        sendEmailAfterServiceReservation(serviceReservationDTO);
    }

    @Override
    public PagedListHolder<ServiceReservation> getServicesByUserOrderByDate(int page, int size, long userId){
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
    public List<UserCarsDTO> getCarByCredentialId(long credentialId) throws ParseException {
        List<UserCarsDTO> userCars = new ArrayList<>();
        String maxMileage = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.");
        Date date;
        date = sdf.parse("2000.01.01.");
        List<Car> carList = serviceReservationRepository.getAllCarByCredentialId(credentialId);
        for (Car car : carList) {
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

    private UserCarsDTO mapDTOtoUserCarsDTO(Car car, Date date, String maxMileage){
        UserCarsDTO userCarsDTO = mapStructObjectMapper.carToUserCarsDTO(car);
        userCarsDTO.setMileage(maxMileage);
        userCarsDTO.setDateOfSet(date);
        return userCarsDTO;
    }

    private ServiceReservation mapDTOtoServiceReservation(ServiceReservationDTO serviceReservationDTO){
        return mapStructObjectMapper.serviceReservationDTOtoServiceReservation(serviceReservationDTO);
    }

    private void sendEmailAfterServiceReservation(ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException {
        Car car = carService.getCarById(serviceReservationDTO.getFkServiceReservationCarId());
        UserDataDTO user = userService.getUserDetailsByCarId(serviceReservationDTO.getFkServiceReservationCarId());
        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .billingZipCode(user.getBillingZipCode())
                .billingTown(user.getBillingTown())
                .billingOtherAddressType(user.getBillingOtherAddressType())
                .billingStreet(user.getBillingStreet())
                .billingName(user.getBillingName())
                .billingPhoneNumber(user.getBillingPhoneNumber())
                .billingEmail(user.getBillingEmail())
                .billingTaxNumber(user.getBillingTaxNumber())
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
