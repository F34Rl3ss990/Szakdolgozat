package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.web.exception.BadRequestException;
import com.EGEA1R.CarService.web.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.*;
import com.EGEA1R.CarService.persistance.repository.interfaces.UserRepository;
import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Validated
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private EmailService emailService;

    private final String regexp = "^[0-9]{8}[-][0-9][-][0-9]{2}$";
    private final String taxNumError = "Tax number is incorrect";

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    @Override
    public void saveUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, Long credentialId, String email){
        if(!unauthorizedUserReservationDTO.getBillingTaxNumber().isEmpty() && Boolean.FALSE.equals(unauthorizedUserReservationDTO.getBillingEuTax())){
            if(Pattern.matches(regexp, unauthorizedUserReservationDTO.getBillingTaxNumber())){
                userRepository.saveUser(mapDTOtoUser(unauthorizedUserReservationDTO), credentialId, email);
            }else
                throw new BadRequestException(taxNumError);
    } else {
            userRepository.saveUser(mapDTOtoUser(unauthorizedUserReservationDTO), credentialId, email);
        }
    }

    @Transactional
    @Override
    public void saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException {
            String services = servicesListToString(unauthorizedUserReservationDTO.getReservedServices());
            if(!unauthorizedUserReservationDTO.getBillingTaxNumber().isEmpty() && Boolean.FALSE.equals(unauthorizedUserReservationDTO.getBillingEuTax())){
                if(Pattern.matches(regexp, unauthorizedUserReservationDTO.getBillingTaxNumber())){
                    if(Boolean.TRUE.equals(CarServiceImpl.checkLicensePlate(unauthorizedUserReservationDTO.getForeignCountryPlate(), unauthorizedUserReservationDTO.getLicensePlateNumber()))){
                        userRepository.saveUnAuthorizedUser(mapDTOtoUser(unauthorizedUserReservationDTO),
                                mapDTOtoCar(unauthorizedUserReservationDTO),
                                mapDTOtoServiceReservation(unauthorizedUserReservationDTO), services);
                        emailService.sendReservedServiceInformation(unauthorizedUserReservationDTO);
                    }
                }else
                    throw new BadRequestException(taxNumError);
            } else{
                userRepository.saveUnAuthorizedUser(mapDTOtoUser(unauthorizedUserReservationDTO),
                        mapDTOtoCar(unauthorizedUserReservationDTO),
                        mapDTOtoServiceReservation(unauthorizedUserReservationDTO), services);
                emailService.sendReservedServiceInformation(unauthorizedUserReservationDTO);
            }
    }

    @Override
    public void modifyUser(ModifyUserDateRequest modifyUserDateRequest){
        if(!modifyUserDateRequest.getBillingTaxNumber().isEmpty() && Boolean.FALSE.equals(modifyUserDateRequest.getBillingEuTax())){
            if(Pattern.matches(regexp, modifyUserDateRequest.getBillingTaxNumber())){
                userRepository.modifyUserData(mapDTOtoUser(modifyUserDateRequest));
            }else
                throw new BadRequestException(taxNumError);
        }
        else{
            userRepository.modifyUserData(mapDTOtoUser(modifyUserDateRequest));
        }
    }

    @Override
    public User getUserDetailsByCredentialId(Long credentialId){
        return userRepository
                .findUserByCredentialId(credentialId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with this id not found: %s", Long.toString(credentialId))));
    }

    @Override
    public User getUserDetailsByCarId(Long carId){
        return userRepository
                .findUserByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with this id not found: %s", Long.toString(carId))));
    }

    @Override
    public PagedListHolder<User> getAllUserPage(int page, int size) {
        List<User> users = userRepository.getAllUser();
        PagedListHolder<User> pageHolder = new PagedListHolder<>(users);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }

    public static String servicesListToString(List<String> reservedServiceLists){
        return reservedServiceLists.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

    }

    private User mapDTOtoUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        BillingInformation billingInformation = modelMapper.map(unauthorizedUserReservationDTO, BillingInformation.class);
        User user = modelMapper.map(unauthorizedUserReservationDTO, User.class);
        user.setBillingInformation(billingInformation);
        return user;
    }

    private User mapDTOtoUser(ModifyUserDateRequest modifyUserDateRequest){
        BillingInformation billingInformation = modelMapper.map(modifyUserDateRequest, BillingInformation.class);
        User user = modelMapper.map(modifyUserDateRequest, User.class);
        user.setBillingInformation(billingInformation);
        return user;
    }

    private Car mapDTOtoCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        Car car = modelMapper.map(unauthorizedUserReservationDTO, Car.class);
        CarMileage carMileage = modelMapper.map(unauthorizedUserReservationDTO, CarMileage.class);
        List<CarMileage> carMileages = new ArrayList<>();
        carMileages.add(carMileage);
        car.setCarMileages(carMileages);
        return car;
    }

    private ServiceReservation mapDTOtoServiceReservation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) {
        return modelMapper.map(unauthorizedUserReservationDTO, ServiceReservation.class);
    }
}
