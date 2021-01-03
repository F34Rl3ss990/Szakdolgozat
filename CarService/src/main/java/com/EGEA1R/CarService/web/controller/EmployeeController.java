package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.interfaces.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService){
        this.employeeService = employeeService;
    }
}
