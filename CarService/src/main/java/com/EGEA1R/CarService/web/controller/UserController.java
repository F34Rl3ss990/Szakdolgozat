package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.authentication.AuthCredentialDetailsImpl;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
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
@CrossOrigin(origins = "*", maxAge = 3600)
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
    public ResponseEntity<?> getUserInformation(){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getUserDetailsByCredentialId(credentialDetails.getCredentialId()));
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addUser(@RequestBody UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        AuthCredentialDetailsImpl credentialDetails = (AuthCredentialDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.saveUser(unauthorizedUserReservationDTO, credentialDetails.getCredentialId(), credentialDetails.getUsername());
        return ResponseEntity.ok(new MessageResponse("User successfully added!"));
    }

    @PostMapping("/modifyUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> modifyUserData(@Valid @RequestBody ModifyUserDateRequest modifyUserDateRequest){
        userService.modifyUser(modifyUserDateRequest);
        return ResponseEntity.ok(new MessageResponse("User data changed successfully!"));
    }

    @GetMapping("/everyUser")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(userService.getAllUserPage(page, size));
    }

}
