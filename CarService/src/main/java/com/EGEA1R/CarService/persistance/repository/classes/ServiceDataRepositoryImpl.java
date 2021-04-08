package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.repository.interfaces.ServiceDataRepository;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ServiceDataRepositoryImpl implements ServiceDataRepository {


    @PersistenceContext
    private EntityManager em;
/*
    @Override
    public void saveServiceData(ServiceDataDTO serviceDataDTO) {
        em.createNativeQuery("insert into service_data(date, bill_num, services_done, comment, mileage) values (?, ?, ?, ?, ?)")
                .setParameter(1, serviceDataDTO.getDate())
                .setParameter(2, serviceDataDTO.getBillNum())
                .setParameter(3, serviceDataDTO.getServicesDone())
                .setParameter(4, serviceDataDTO.getComment())
                .setParameter(5, serviceDataDTO.getMileage())
                .executeUpdate();
    }

 */

    @Transactional
    @Override
    public Long saveServiceData(ServiceDataDTO serviceDataDTO, Long financeId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SAVE_SERVICE_DATA_OUT_ID");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(6, Long.class, ParameterMode.OUT);
        query.setParameter(1, serviceDataDTO.getBillNum());
        query.setParameter(2, serviceDataDTO.getServicesDone());
        query.setParameter(3, serviceDataDTO.getComment());
        query.setParameter(4, serviceDataDTO.getMileage());
        query.setParameter(5, financeId);
        Long serviceDataId = (Long) query.getOutputParameterValue(6);
        query.executeUpdate();
        return serviceDataId;
    }


    @Override
    public List<ServiceDataDTO> getAllServiceByUser(Long credentialId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_SERVICE_DATA_BY_USER_ORDER_BY_DATE", "GetServiceDataByUser");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, credentialId);
        return query.getResultList();
    }

    @Override
    public List<ServiceDataDTO> getAllServiceByCar(Long carId, Long credentialId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_SERVICE_DATA_BY_CAR_ORDER_BY_DATE", "GetServiceData");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        query.setParameter(1, carId);
        query.setParameter(2, credentialId);
        return query.getResultList();
    }
}
