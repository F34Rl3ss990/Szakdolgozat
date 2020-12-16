package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService{

    private StockRepository stockRepository;

    @Autowired
    public void setStockRepository(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }
}
