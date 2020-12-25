package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.interfaces.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FinanceController {

    private FinanceService financeService;

    @Autowired
    public void setFinanceService(FinanceService financeService){
        this.financeService = financeService;
    }
}
