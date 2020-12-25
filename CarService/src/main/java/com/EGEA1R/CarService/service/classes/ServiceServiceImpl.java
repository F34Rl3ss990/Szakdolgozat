package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.repository.ServiceRepository;
import com.EGEA1R.CarService.service.interfaces.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;

    @Autowired
    public void setServiceRepository(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }
}
