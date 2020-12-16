package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.WorkdayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WorkdayController {

    private WorkdayService workdayService;

    @Autowired
    public void setWorkdayService(WorkdayService workdayService){
        this.workdayService = workdayService;
    }
}
