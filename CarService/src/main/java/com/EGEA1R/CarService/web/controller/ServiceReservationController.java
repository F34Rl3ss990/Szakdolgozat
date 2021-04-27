package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.service.interfaces.ExcelService;
import com.EGEA1R.CarService.service.interfaces.ServiceReservationService;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.ExcelCarDTO;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.UserCarsDTO;
import com.EGEA1R.CarService.web.DTO.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/test/serviceReservation")
public class ServiceReservationController {

    private ServiceReservationService serviceReservationService;

    private UserService userService;

    private ExcelService excelService;

    @Autowired
    public void setServiceService(ServiceReservationService serviceReservationService){
        this.serviceReservationService = serviceReservationService;
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setExcelService(ExcelService excelService){
        this.excelService = excelService;
    }

    @PostMapping("/serviceReservationUnauthorizedValidation")
    public Boolean authorizedUserReservationValidation(@Valid @RequestBody UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) {
        return true;
    }

    @PostMapping("/serviceReservationAuthorizedValidation")
    @PreAuthorize("hasRole('USER')")
    public User authorizedUserReservationValidation(@Valid @RequestBody ServiceReservationDTO serviceReservationDTO) {
        return userService.getUserByUserId(serviceReservationDTO.getFkCarUserId());
    }

    @PostMapping("/serviceReservationUnauthorized")
    public ResponseEntity<?> unauthorizedUserReservation(@RequestBody UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException {
        userService.saveUnauthorizedUser(unauthorizedUserReservationDTO);
        return ResponseEntity.ok(new MessageResponse("Successfully reserved!"));
    }

    @PostMapping("/reserveService")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reserveService(@RequestBody ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException {
        serviceReservationService.saveService(serviceReservationDTO);
        return ResponseEntity.ok("Successfully reserved!");
    }

    @GetMapping("/getServicesByUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getServicesByUser(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestBody Long userId){
        return ResponseEntity.ok(serviceReservationService.getServicesByUserOrderByDate(page, size, userId));
    }

    @GetMapping("/getServicesToday")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEveryServiceToday(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(serviceReservationService.getServicesTodayOrderByDate(page, size));
    }

    @GetMapping("/reserveDataGetter")
    public List<ExcelCarDTO> test() {
        return excelService.getExcelData();
    }

    @GetMapping("/getCarsByRegId")
    public List<UserCarsDTO> getCarsByCredentialId(@RequestParam ("credentialId") Long credentialId) throws ParseException {
        return serviceReservationService.getCarByCredentialId(credentialId);
    }



}
