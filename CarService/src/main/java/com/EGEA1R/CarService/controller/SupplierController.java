package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SupplierController {

    private SupplierService supplierService;

    @Autowired
    public void setSupplierService(SupplierService supplierService){
        this.supplierService = supplierService;
    }
}
