package com.car_service.egea1r.web.controller;

import com.car_service.egea1r.persistance.entity.User;
import com.car_service.egea1r.service.authentication.AuthCredentialDetailsImpl;
import com.car_service.egea1r.service.interfaces.UserService;
import com.car_service.egea1r.validation.annotation.ValidPhoneNumber;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.payload.request.ModifyUserDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private static final String  USER_DATA_CHANGE = "User data changed successfully!";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public User getUserInformation(){
        AuthCredentialDetailsImpl credentialDetails = CredentialController.getAuthPrincipal();
        return userService.getUserDetailsByCredentialId(credentialDetails.getCredentialId());
    }

    @PostMapping("/changeBillingData")
    @PreAuthorize("hasRole('USER')")
    public String modifyBillingData(@Valid @RequestBody ModifyUserDataRequest modifyUserDataRequest){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.modifyUser(modifyUserDataRequest, credentialDetails.getCredentialId());
        return USER_DATA_CHANGE;
    }

    @PostMapping("/changePhoneNumber")
    @PreAuthorize("hasRole('USER')")
    public String modifyUserPhoneNumber(@ValidPhoneNumber @RequestBody String phoneNumber){
        AuthCredentialDetailsImpl credentialDetails = CredentialController.getAuthPrincipal();
        userService.modifyPhoneNumber(phoneNumber, credentialDetails.getCredentialId());
        return USER_DATA_CHANGE;
    }

    @PostMapping("/addCarAndUser")
    @PreAuthorize("hasRole('USER')")
    public String addCarAndUser(@Valid @RequestBody CarAndUserDTO carAndUserDTO){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.addCarAndUser(carAndUserDTO, credentialDetails.getCredentialId());
        return USER_DATA_CHANGE;
    }

    @GetMapping("/everyUser")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedListHolder<User> getAllUserPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size){
        return userService.getAllUserPage(page, size);
    }
}
