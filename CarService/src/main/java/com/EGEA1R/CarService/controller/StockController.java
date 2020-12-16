package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StockController {

    private StockService stockService;

    @Autowired
    public void setStockService(StockService stockService){
        this.stockService = stockService;
    }
}
