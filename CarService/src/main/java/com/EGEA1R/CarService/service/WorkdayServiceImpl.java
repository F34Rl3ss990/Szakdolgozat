package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.repository.WorkdayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkdayServiceImpl implements  WorkdayService{

    private WorkdayRepository workdayRepository;

    @Autowired
    public void setWorkdayRepository(WorkdayRepository workdayRepository){
        this.workdayRepository = workdayRepository;
    }
}
