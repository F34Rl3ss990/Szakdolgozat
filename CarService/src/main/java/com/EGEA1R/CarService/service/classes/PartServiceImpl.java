package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.repository.PartRepository;
import com.EGEA1R.CarService.service.interfaces.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl implements PartService {

    private PartRepository partRepository;

    @Autowired
    public void setPartRepository(PartRepository partRepository){
        this.partRepository = partRepository;
    }
}
