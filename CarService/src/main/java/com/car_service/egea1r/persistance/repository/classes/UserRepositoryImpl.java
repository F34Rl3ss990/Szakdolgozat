package com.car_service.egea1r.persistance.repository.classes;

import com.car_service.egea1r.persistance.entity.Car;
import com.car_service.egea1r.persistance.entity.ServiceReservation;
import com.car_service.egea1r.persistance.entity.User;
import com.car_service.egea1r.persistance.repository.interfaces.UserRepository;
import com.car_service.egea1r.web.data.DTO.CarAndUserDTO;
import com.car_service.egea1r.web.data.DTO.UserDataDTO;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Validated
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void saveUnAuthorizedUser(User user, Car car, ServiceReservation serviceReservation) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SAVE_UNAUTHORIZED_USER");
                query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(6, Integer.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(20, Date.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
                query.setParameter(1, user.getName());
                query.setParameter(2, user.getEmail());
                query.setParameter(3, user.getPhoneNumber());
                query.setParameter(4, user.getBillingInformation().getBillingName());
                query.setParameter(5, user.getBillingInformation().getBillingPhoneNumber());
                query.setParameter(6, user.getBillingInformation().getBillingZipCode());
                query.setParameter(7, user.getBillingInformation().getBillingTown());
                query.setParameter(8, user.getBillingInformation().getBillingStreet());
                query.setParameter(9, user.getBillingInformation().getBillingOtherAddressType());
                query.setParameter(10, user.getBillingInformation().getBillingTaxNumber());
                query.setParameter(11, user.getBillingInformation().getBillingEmail());
                query.setParameter(12, car.getBrand());
                query.setParameter(13, car.getType());
                query.setParameter(14, car.getEngineType());
                query.setParameter(15, car.getYearOfManufacture());
                query.setParameter(16, car.getEngineNumber());
                query.setParameter(17, car.getChassisNumber());
                query.setParameter(18, car.getCarMileages().get(0).getMileage());
                query.setParameter(19, car.getLicensePlateNumber());
                query.setParameter(20, serviceReservation.getReservedDate());
                query.setParameter(21, serviceReservation.getComment());
                query.setParameter(22, serviceReservation.getReservedServices());
                query.executeUpdate();
    }

    @Transactional
    @Override
    public void modifyUserData(User user, long userId) {
        em.createNativeQuery("UPDATE user " +
                "set billing_name = ?, billing_phone_number = ?," +
                " billing_zip_code = ?, billing_town = ?, billing_street = ?," +
                " billing_other_address_type = ?, billing_email = ? where user.user_id = ?")
                .setParameter(1, user.getBillingInformation().getBillingName())
                .setParameter(2, user.getBillingInformation().getBillingPhoneNumber())
                .setParameter(3, user.getBillingInformation().getBillingZipCode())
                .setParameter(4, user.getBillingInformation().getBillingTown())
                .setParameter(5, user.getBillingInformation().getBillingStreet())
                .setParameter(6, user.getBillingInformation().getBillingOtherAddressType())
                .setParameter(7, user.getBillingInformation().getBillingEmail())
                .setParameter(8, userId)
                .executeUpdate();
    }

    @Override
    public Optional<User> findUserByCredentialId(long credentialId) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("GET_USER_BY_CREDENTIAL_ID", "GetUserByFkId");
            query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
            query.setParameter(1, credentialId);
            query.execute();
            return Optional.of((User) query.getSingleResult());
        } catch(NoResultException n){
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUser() {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_ALL_USER", "GetUserByPermission");
        return query.getResultList();
    }

    @Override
    public Optional<UserDataDTO> findUserByCarId(long carId) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("GET_USER_DETAILS_BY_FK_CAR_ID", "GetUserDetailsByFkCarId");
            query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
            query.setParameter(1, carId);
            return Optional.of((UserDataDTO) query.getSingleResult());
        }catch (NoResultException n){
            return Optional.empty();
        }
    }

    @Override
    public User getUserByCarId(long carId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("GET_USER_BY_CAR_ID", "GetUserByFkId");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, carId);
        return (User) query.getSingleResult();
    }

    @Override
    @Transactional
    public void modifyPhoneNumber(String phoneNumber, long userId) {
        em.createNativeQuery("UPDATE user " +
                "set phone_number = ? where user.user_id = ?")
                .setParameter(1, phoneNumber)
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Transactional
    @Override
    public void addCarAndUser(CarAndUserDTO carAndUserDTO, long credentialId, String email) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("ADD_USER_AND_CAR");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(7, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
        query.setParameter(1, credentialId);
        query.setParameter(2, carAndUserDTO.getName());
        query.setParameter(3, email);
        query.setParameter(4, carAndUserDTO.getPhoneNumber());
        query.setParameter(5, carAndUserDTO.getBillingName());
        query.setParameter(6, carAndUserDTO.getBillingPhoneNumber());
        query.setParameter(7, carAndUserDTO.getBillingZipCode());
        query.setParameter(8, carAndUserDTO.getBillingTown());
        query.setParameter(9, carAndUserDTO.getBillingStreet());
        query.setParameter(10, carAndUserDTO.getBillingOtherAddressType());
        query.setParameter(11, carAndUserDTO.getBillingTaxNumber());
        query.setParameter(12, carAndUserDTO.getBillingEmail());
        query.setParameter(13, carAndUserDTO.getBrand());
        query.setParameter(14, carAndUserDTO.getType());
        query.setParameter(15, carAndUserDTO.getEngineType());
        query.setParameter(16, carAndUserDTO.getYearOfManufacture());
        query.setParameter(17, carAndUserDTO.getEngineNumber());
        query.setParameter(18, carAndUserDTO.getChassisNumber());
        query.setParameter(19, carAndUserDTO.getMileage());
        query.setParameter(20, carAndUserDTO.getLicensePlateNumber());
        query.executeUpdate();
    }

    @Override
    public long findUserIdByCredentialId(long credentialId) {
        Query query = em.createNativeQuery("SELECT user_id from user where fk_user_credential = ?");
        query.setParameter(1, credentialId);
        return ((BigInteger) query.getSingleResult()).longValue();
    }

}

