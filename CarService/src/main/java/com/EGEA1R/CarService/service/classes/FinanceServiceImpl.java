package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.FinanceRepository;
import com.EGEA1R.CarService.service.interfaces.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements FinanceService {

    private FinanceRepository financeRepository;

    @Autowired
    public void setFinanceRepository(FinanceRepository financeRepository){
        this.financeRepository = financeRepository;
    }
}
