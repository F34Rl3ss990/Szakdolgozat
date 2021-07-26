package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.web.DTO.CarAndUserDTO;
import com.EGEA1R.CarService.web.DTO.payload.UserDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.support.PagedListHolder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {

    void saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException;

    void modifyUser(ModifyUserDateRequest modifyUserDateRequest, Long credentialId);

    User getUserDetailsByCredentialId(Long credentialId);

    PagedListHolder<User> getAllUserPage(int page, int size);

    UserDataDTO getUserDetailsByCarId(Long carId);

    User getUserByCarId(Long userId);

    void modifyPhoneNumber(String phoneNumber, Long credentialId);

    void addCarAndUser(CarAndUserDTO carAndUserDTO, Long credentialId);
}
