package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.User;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import com.car_service.egea1r.web.data.payload.request.ModifyUserDateRequest;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.support.PagedListHolder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {

    void saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException;

    void modifyUser(ModifyUserDateRequest modifyUserDateRequest, long credentialId);

    User getUserDetailsByCredentialId(long credentialId);

    PagedListHolder<User> getAllUserPage(int page, int size);

    UserDataDTO getUserDetailsByCarId(long carId);

    User getUserByCarId(long userId);

    void modifyPhoneNumber(String phoneNumber, long credentialId);

    void addCarAndUser(CarAndUserDTO carAndUserDTO, long credentialId);
}
