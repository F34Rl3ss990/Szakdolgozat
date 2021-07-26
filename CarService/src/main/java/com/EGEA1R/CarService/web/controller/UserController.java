package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.validation.annotation.ValidPhoneNumber;
import com.EGEA1R.CarService.web.DTO.CarAndUserDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.PhoneNumber;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/test/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getUser")
    @PreAuthorize("hasRole('USER')")
    public User getUserInformation(){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUserDetailsByCredentialId(credentialDetails.getCredentialId());
    }

    @PostMapping("/changeBillingData")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> modifyBillingData(@Valid @RequestBody ModifyUserDateRequest modifyUserDateRequest){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.modifyUser(modifyUserDateRequest, credentialDetails.getCredentialId());
        return ResponseEntity.ok(new MessageResponse("User data changed successfully!"));
    }

    @PostMapping("/changePhoneNumber")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> modifyUserPhoneNumber(@ValidPhoneNumber @RequestBody PhoneNumber phoneNumber){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.modifyPhoneNumber(phoneNumber.getPhoneNumber(), credentialDetails.getCredentialId());
        return ResponseEntity.ok(new MessageResponse("User data changed successfully!"));
    }

    @PostMapping("/addCarAndUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addCarAndUser(@Valid @RequestBody CarAndUserDTO carAndUserDTO){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.addCarAndUser(carAndUserDTO, credentialDetails.getCredentialId());
        return ResponseEntity.ok(new MessageResponse("User data changed successfully!"));
    }

    @GetMapping("/everyUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(userService.getAllUserPage(page, size));
    }

}
