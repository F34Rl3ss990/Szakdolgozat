package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.interfaces.PartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PartController {

    private PartService partService;

    @Autowired
    public void setPartService(PartService partService){
        this.partService = partService;
    }
}
