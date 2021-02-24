package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.interfaces.ServiceReservationService;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/serviceReservation")
public class ServiceReservationController {

    private ServiceReservationService serviceReservationService;

    private UserService userService;

    @Autowired
    public void setServiceService(ServiceReservationService serviceReservationService){
        this.serviceReservationService = serviceReservationService;
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/serviceReservationUnauthorized")
    public ResponseEntity<?> unauthorizedUserReservation(@Valid @RequestBody UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException {
        userService.saveUnauthorizedUser(unauthorizedUserReservationDTO);
        return ResponseEntity.ok("Successfully reserved!");
    }

    @PostMapping("/reserveService")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reserveService(@Valid @RequestBody ServiceReservationDTO serviceReservationDTO) throws MessagingException {
        serviceReservationService.saveService(serviceReservationDTO);
        return ResponseEntity.ok("Successfully reserved!");
    }

    @GetMapping("/getServicesByUser")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getServicesByUser(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestBody Long userId){
        return ResponseEntity.ok(serviceReservationService.getServicesByUserOrderByDate(page, size, userId));
    }

    @GetMapping("/getServicesToday")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEveryServiceToday(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(serviceReservationService.getServicesTodayOrderByDate(page, size));
    }



}
