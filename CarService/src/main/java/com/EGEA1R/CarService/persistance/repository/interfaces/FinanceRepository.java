package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.web.DTO.FinanceDTO;

public interface FinanceRepository {

    Long saveFinanceByServiceData(FinanceDTO financeDTO);
}
