package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.data.domain.Page;

public interface UserService {

    void saveUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, Long credentialId);

    User saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO);

    void modifyUser(ModifyUserDateRequest modifyUserDateRequest, Long userId, Long credentialId);

    User getUserDetails(Long credentialId);

    Page<User> getAllUserPage(int page, int size);
}
