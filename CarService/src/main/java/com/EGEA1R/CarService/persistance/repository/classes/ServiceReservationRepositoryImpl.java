package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.repository.interfaces.ServiceReservationRepository;
import com.EGEA1R.CarService.web.DTO.UserCarsDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigInteger;
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

    @Transactional
    @Override
    public void setServiceDataFk(Long serviceDataFk, Long carId) {
        Query query = em.createNativeQuery("select service_reservation_id from service_reservation inner join car on service_reservation.fk_service_reservation_car = car.car_id where service_reservation.fk_service_reservation_service_data is null and fk_service_reservation_car = ? ")
                .setParameter(1, carId);
        Long serviceReservationId = ((BigInteger) query.getSingleResult()).longValue();
        em.createNativeQuery("update service_reservation set fk_service_reservation_service_data = ? where service_reservation_id = ? ")
                .setParameter(1, serviceDataFk)
                .setParameter(2, serviceReservationId)
                .executeUpdate();
    }


}
