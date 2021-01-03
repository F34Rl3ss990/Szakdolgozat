package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.WorkdayRepository;
import com.EGEA1R.CarService.service.interfaces.WorkdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkdayServiceImpl implements WorkdayService {

    private WorkdayRepository workdayRepository;

    @Autowired
    public void setWorkdayRepository(WorkdayRepository workdayRepository){
        this.workdayRepository = workdayRepository;
    }
}
