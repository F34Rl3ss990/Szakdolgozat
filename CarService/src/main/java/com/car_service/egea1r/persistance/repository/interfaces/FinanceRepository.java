package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.web.data.DTO.FinanceDTO;

public interface FinanceRepository {

    long saveFinanceByServiceData(FinanceDTO financeDTO);
}
