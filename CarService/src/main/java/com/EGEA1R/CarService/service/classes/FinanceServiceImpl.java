package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements FinanceService{

    private FinanceRepository financeRepository;

    @Autowired
    public void setFinanceRepository(FinanceRepository financeRepository){
        this.financeRepository = financeRepository;
    }
}
