package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.interfaces.UserRepository;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
    public void saveUser(@Valid User user, Long credentialId, String email) {
        em.createNativeQuery("INSERT INTO user (first_name, last_name, e_mail, phone_number, billing_name, " +
                "billing_phone_number, billing_zip_code, billing_town, billing_country, billing_street, billing_other_address_type," +
                " billing_tax_number, billing_email, fk_user_credential)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, user.getFirstName())
                .setParameter(2, user.getLastName())
                .setParameter(3, email)
                .setParameter(4, user.getPhoneNumber().toString())
                .setParameter(5, user.getBillingInformation().getBillingName())
                .setParameter(6, user.getBillingInformation().getBillingPhoneNumber().toString())
                .setParameter(7, user.getBillingInformation().getBillingZipCode())
                .setParameter(8, user.getBillingInformation().getBillingTown())
                .setParameter(9, user.getBillingInformation().getBillingCountry())
                .setParameter(10, user.getBillingInformation().getBillingStreet())
                .setParameter(11, user.getBillingInformation().getBillingOtherAddressType())
                .setParameter(12, user.getBillingInformation().getBillingTaxNumber())
                .setParameter(13, user.getBillingInformation().getBillingEmail())
                .setParameter(14, credentialId)
                .executeUpdate();
    }

    @Transactional
    @Override
    public void saveUnAuthorizedUser(User user, Car car, ServiceReservation serviceReservation, String services) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SAVE_UNAUTHORIZED_USER");
                query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
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
                query.registerStoredProcedureParameter(20, Integer.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(22, Date.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
                query.setParameter(1, user.getFirstName());
                query.setParameter(2, user.getLastName());
                query.setParameter(3, user.getEmail());
                query.setParameter(4, user.getPhoneNumber().toString());
                query.setParameter(5, user.getBillingInformation().getBillingName());
                query.setParameter(6, user.getBillingInformation().getBillingPhoneNumber().toString());
                query.setParameter(7, user.getBillingInformation().getBillingZipCode());
                query.setParameter(8, user.getBillingInformation().getBillingTown());
                query.setParameter(9, user.getBillingInformation().getBillingCountry());
                query.setParameter(10, user.getBillingInformation().getBillingStreet());
                query.setParameter(11, user.getBillingInformation().getBillingOtherAddressType());
                query.setParameter(12, user.getBillingInformation().getBillingTaxNumber());
                query.setParameter(13, user.getBillingInformation().getBillingEmail());
                query.setParameter(14, car.getBrand());
                query.setParameter(15, car.getType());
                query.setParameter(16, car.getEngineType());
                query.setParameter(17, car.getYearOfManufacture());
                query.setParameter(18, car.getEngineNumber());
                query.setParameter(19, car.getChassisNumber());
                query.setParameter(20, car.getCarMileages().get(0).getMileage());
                query.setParameter(21, car.getLicensePlateNumber());
                query.setParameter(22, serviceReservation.getReservedDate());
                query.setParameter(23, serviceReservation.getComment());
                query.setParameter(24, services);
                query.executeUpdate();
    }

    @Transactional
    @Override
    public void modifyUserData(User user) {
        em.createNativeQuery("UPDATE user " +
                "set phone_number = ?, billing_name = ?, billing_phone_number = ?," +
                " billing_zip_code = ?, billing_town = ?, billing_country = ?, billing_street = ?," +
                " billing_other_address_type = ?, billing_email = ?" +
                " where user_id = ?")
                .setParameter(1, user.getPhoneNumber())
                .setParameter(2, user.getBillingInformation().getBillingName())
                .setParameter(3, user.getBillingInformation().getBillingPhoneNumber())
                .setParameter(4, user.getBillingInformation().getBillingZipCode())
                .setParameter(5, user.getBillingInformation().getBillingTown())
                .setParameter(6, user.getBillingInformation().getBillingCountry())
                .setParameter(7, user.getBillingInformation().getBillingStreet())
                .setParameter(8, user.getBillingInformation().getBillingOtherAddressType())
                .setParameter(9, user.getBillingInformation().getBillingEmail())
                .setParameter(10, user.getUserId())
                .executeUpdate();
    }

    @Override
    public Optional<User> findUserByCredentialId(Long credentialId) {
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
        return (List<User>) query.getResultList();
    }

    @Override
    public Optional<User> findUserByCarId(Long carId) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("GET_USER_DETAILS_BY_FK_CAR_ID", "GetUserDetailsByFkCarId");
            query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
            query.setParameter(1, carId);
            return Optional.of((User) query.getSingleResult());
        }catch (NoResultException n){
            return Optional.empty();
        }
    }

}
