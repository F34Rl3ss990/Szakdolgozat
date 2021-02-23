package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.repository.interfaces.CarRepository;
import com.EGEA1R.CarService.web.DTO.CarDTO;
import org.springframework.stereotype.Repository;

import javax.mail.Store;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepositoryImpl implements CarRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void addCar(Car car) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SAVE_CAR");
        for(int i = 1; i<8; i++) {
             query.registerStoredProcedureParameter(i, String.class, ParameterMode.IN);
        }
        query.registerStoredProcedureParameter(8, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(9, Integer.class, ParameterMode.IN);
        query.setParameter(1, car.getBrand());
        query.setParameter(2, car.getType());
        query.setParameter(3, car.getEngineType());
        query.setParameter(4, car.getYearOfManufacture());
        query.setParameter(5, car.getEngineNumber());
        query.setParameter(6, car.getChassisNumber());
        query.setParameter(7, car.getLicensePlateNumber().toUpperCase());
        query.setParameter(8, car.getFkCarUserId());
        query.setParameter(9, car.getCarMileages().get(0).getMileage());
        query.execute();
    }

    @Transactional
    @Override
    public void modifyCarById(Car car) {
        em.createNativeQuery("UPDATE car set engine_number = ?, engine_type = ?, license_plate_number = ? where car_id = ? ")
                .setParameter(1, car.getEngineNumber())
                .setParameter(2, car.getEngineType())
                .setParameter(3, car.getLicensePlateNumber().toUpperCase())
                .setParameter(4, car.getCarId())
                .executeUpdate();
        em.createNativeQuery("INSERT INTO car_mileage (mileage, fk_car_mileage_car) values (?, ?)")
                .setParameter(1, car.getCarMileages().get(0).getMileage())
                .setParameter(2, car.getCarId())
                .executeUpdate();
    }

    @Transactional
    @Override
    public void deleteCarById(Long carId){
        em.createNativeQuery("delete from service_reservation where fk_service_reservation_car = ?")
                .setParameter(1, carId)
                .executeUpdate();
        em.createNativeQuery("delete from car_mileage where fk_car_mileage_car = ?")
                .setParameter(1, carId)
                .executeUpdate();
        em.createNativeQuery("delete from car where car_id = ?")
                .setParameter(1, carId)
                .executeUpdate();


    }

    @Override
    public List<Car> getAllCarByUser(Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_ALL_CAR_BY_USER", "GetCars");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, userId);
        return (List<Car>) query.getResultList();
    }

    @Override
    public Optional<Car> finCarByCarId(Long carId) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("GET_CAR_BY_CAR_ID", "GetCarByCarId");
            query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
            query.setParameter(1, carId);
            return Optional.of((Car) query.getSingleResult());
        }catch (NoResultException n){
            return Optional.empty();
        }
    }


}
