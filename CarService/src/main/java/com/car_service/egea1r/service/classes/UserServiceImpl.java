package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.*;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.data.payload.request.ModifyUserDataRequest;
import com.car_service.egea1r.web.exception.ResourceNotFoundException;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.service.interfaces.EmailService;
import com.car_service.egea1r.service.interfaces.UserService;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapStructObjectMapper mapStructObjectMapper;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MapStructObjectMapper mapStructObjectMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.mapStructObjectMapper = mapStructObjectMapper;
        this.emailService = emailService;
    }


    @Transactional
    @Async
    @Override
    public void saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException {
        mileageSetter(unauthorizedUserReservationDTO);
        userRepository.saveUnAuthorizedUser(mapDTOtoUser(unauthorizedUserReservationDTO),
                mapDTOtoCar(unauthorizedUserReservationDTO),
                mapDTOtoServiceReservation(unauthorizedUserReservationDTO));
        emailService.sendReservedServiceInformation(unauthorizedUserReservationDTO);
    }

    @Override
    public void modifyUser(ModifyUserDataRequest modifyUserDataRequest, long credentialId){
            userRepository.modifyUserData(mapDTOtoUser(modifyUserDataRequest), userRepository.findUserIdByCredentialId(credentialId));
    }

    @Override
    public User getUserDetailsByCredentialId(long credentialId){
        return userRepository
                .findUserByCredentialId(credentialId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with this id not found: %s", Long.toString(credentialId))));
    }

    @Override
    public UserDataDTO getUserDetailsByCarId(long carId){
        return userRepository
                .findUserByCarId(carId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with this id not found: %s", Long.toString(carId))));
    }

    @Override
    public User getUserByCarId(long carId) {
        return userRepository.getUserByCarId(carId);
    }

    @Override
    public void modifyPhoneNumber(String phoneNumber, long credentialId) {
        userRepository.modifyPhoneNumber(phoneNumber, userRepository.findUserIdByCredentialId(credentialId));
    }

    @Override
    public void addCarAndUser(CarAndUserDTO carAndUserDTO, long credentialId) {
         User user = userRepository.findUserByCredentialId(credentialId).
                  orElseThrow(() -> new ResourceNotFoundException(String.format("User with this credentialId not found: %s", Long.toString(credentialId))));;
        userRepository.addCarAndUser(carAndUserDTO, credentialId, user.getEmail());
    }

    @Override
    public PagedListHolder<User> getAllUserPage(int page, int size) {
        List<User> users = userRepository.getAllUser();
        PagedListHolder<User> pageHolder = new PagedListHolder<>(users);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }


    private User mapDTOtoUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        BillingInformation billingInformation = mapStructObjectMapper.unauthorizedUserReservationDTOtoBillingInformation(unauthorizedUserReservationDTO);
        User user = mapStructObjectMapper.unauthorizedUserReservationDTOtoUser(unauthorizedUserReservationDTO);
        user.setBillingInformation(billingInformation);
        return user;
    }

    private User mapDTOtoUser(ModifyUserDataRequest modifyUserDataRequest){
        BillingInformation billingInformation = mapStructObjectMapper.modifyUserDataRequestToBillingInformation(modifyUserDataRequest);
        User user = mapStructObjectMapper.modifyUserDataRequestToUser(modifyUserDataRequest);
        user.setBillingInformation(billingInformation);
        return user;
    }

    private Car mapDTOtoCar(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        Car car = mapStructObjectMapper.unauthorizedUserReservationDTOtoCar(unauthorizedUserReservationDTO);
        CarMileage carMileage = mapStructObjectMapper.unauthorizedUserReservationDTOtoCarMileage(unauthorizedUserReservationDTO);
        List<CarMileage> carMileages = new ArrayList<>();
        carMileages.add(carMileage);
        car.setCarMileages(carMileages);
        return car;
    }

    private ServiceReservation mapDTOtoServiceReservation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) {
       return mapStructObjectMapper.unauthorizedUserReservationDTOtoServiceReservation(unauthorizedUserReservationDTO);
    }

    private void mileageSetter(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        if (unauthorizedUserReservationDTO.getMileage() == null) {
            unauthorizedUserReservationDTO.setMileage("");
        }
    }
}
