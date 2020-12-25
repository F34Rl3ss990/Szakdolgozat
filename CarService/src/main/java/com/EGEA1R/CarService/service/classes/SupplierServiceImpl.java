package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.repository.SupplierRepository;
import com.EGEA1R.CarService.service.interfaces.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;

    @Autowired
    public void setSupplierRepository(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }
}
