package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.repository.interfaces.ServiceReservationRepository;
import com.EGEA1R.CarService.web.DTO.UserCarsDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class ServiceReservationRepositoryImpl implements ServiceReservationRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void saveService(ServiceReservation serviceReservation) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SAVE_SERVICE_RESERVATION");
        query.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, Long.class, ParameterMode.IN);
        query.setParameter(1, serviceReservation.getReservedDate());
        query.setParameter(2, serviceReservation.getComment());
        query.setParameter(3, serviceReservation.getReservedServices());
        query.setParameter(4, serviceReservation.getFkServiceReservationCarId());
        query.executeUpdate();
    }

    @Override
    public List<ServiceReservation> getAllServiceByUser(Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_SERVICES_BY_USER_ORDER_BY_DATE", "GetServicesByUser");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    @Override
    public List<ServiceReservation> getAllServiceByTodayDate(LocalDate localDate) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_SERVICES_TODAY", "GetServicesByToday");
        query.registerStoredProcedureParameter(1, LocalDate.class, ParameterMode.IN);
        query.setParameter(1, localDate);
        return query.getResultList();
    }

    @Override
    public List<Car> getAllCarByCredentialId(Long credentialId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_CARS_BY_CREDENTIAL_ID", "GetCars");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, credentialId);
        return query.getResultList();
    }
}
