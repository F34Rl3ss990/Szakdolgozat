package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.support.PagedListHolder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {

    void saveUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, Long credentialId, String email);

    void saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException;

    void modifyUser(ModifyUserDateRequest modifyUserDateRequest);

    User getUserDetailsByCredentialId(Long credentialId);

    PagedListHolder<User> getAllUserPage(int page, int size);

    User getUserDetailsByCarId(Long carId);

    User getUserByUserId(Long userId);
}
