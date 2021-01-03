package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.EmployeeRepository;
import com.EGEA1R.CarService.service.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }
}
