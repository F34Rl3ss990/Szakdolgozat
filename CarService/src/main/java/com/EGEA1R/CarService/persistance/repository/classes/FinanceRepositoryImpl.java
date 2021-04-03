package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.repository.interfaces.FinanceRepository;
import com.EGEA1R.CarService.web.DTO.FinanceDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

@Repository
public class FinanceRepositoryImpl implements FinanceRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Long saveFinanceByServiceData(FinanceDTO financeDTO) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SAVE_FINANCE_OUT_ID");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);
        query.setParameter(1, financeDTO.getAmount());
        Long financeId = (Long) query.getOutputParameterValue(2);
        query.executeUpdate();
        return financeId;
    }
}
