package com.car_service.egea1r.web.controller;

import com.car_service.egea1r.persistance.entity.ServiceReservation;
import com.car_service.egea1r.persistance.entity.User;
import com.car_service.egea1r.service.interfaces.ExcelService;
import com.car_service.egea1r.service.interfaces.ServiceReservationService;
import com.car_service.egea1r.service.interfaces.UserService;
import com.car_service.egea1r.web.data.DTO.ExcelCarDTO;
import com.car_service.egea1r.web.data.DTO.ServiceReservationDTO;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import com.car_service.egea1r.web.data.DTO.UserCarsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/serviceReservation")
public class ServiceReservationController {

    private final ServiceReservationService serviceReservationService;

    private final UserService userService;

    private final ExcelService excelService;

    @Autowired
    public ServiceReservationController(ServiceReservationService serviceReservationService, UserService userService, ExcelService excelService) {
        this.serviceReservationService = serviceReservationService;
        this.userService = userService;
        this.excelService = excelService;
    }

    @PostMapping("/serviceReservationUnauthorizedValidation")
    public boolean authorizedUserReservationValidation(@Valid @RequestBody UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) {
        return true;
    }

    @PostMapping("/serviceReservationAuthorizedValidation")
    @PreAuthorize("hasRole('USER')")
    public User authorizedUserReservationValidation(@Valid @RequestBody ServiceReservationDTO serviceReservationDTO) {
        return userService.getUserByCarId(serviceReservationDTO.getCarId());
    }

    @PostMapping("/serviceReservationUnauthorized")
    @ResponseStatus(HttpStatus.OK)
    public String unauthorizedUserReservation(@Valid @RequestBody UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException {
        userService.saveUnauthorizedUser(unauthorizedUserReservationDTO);
        return "Successfully reserved!";
    }

    @PostMapping("/reserveService")
    @PreAuthorize("hasRole('USER')")
    public void reserveService(@RequestBody ServiceReservationDTO serviceReservationDTO) throws MessagingException, UnsupportedEncodingException {
        serviceReservationService.saveService(serviceReservationDTO);
    }

    @GetMapping("/getServicesByUser")
    @PreAuthorize("hasRole('USER')")
    public PagedListHolder<ServiceReservation> getServicesByUser(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestBody long userId){
        return serviceReservationService.getServicesByUserOrderByDate(page, size, userId);
    }

    @GetMapping("/getServicesToday")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedListHolder<ServiceReservation> getEveryServiceToday(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size){
        return serviceReservationService.getServicesTodayOrderByDate(page, size);
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
