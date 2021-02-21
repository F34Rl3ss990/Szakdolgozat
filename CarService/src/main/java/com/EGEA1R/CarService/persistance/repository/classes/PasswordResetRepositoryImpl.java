package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.PasswordReset;
import com.EGEA1R.CarService.persistance.repository.interfaces.PasswordResetRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

@Repository
public class PasswordResetRepositoryImpl implements PasswordResetRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void savePasswordResetToken(PasswordReset passwordReset, Long credentialId){
        em.createNativeQuery("INSERT into passwordreset (token, expiry_date, fk_passwordreset_credential_id)" +
                "values (?, ?, ?)")
                .setParameter(1, passwordReset.getToken())
                .setParameter(2, passwordReset.getExpiryDate())
                .setParameter(3, credentialId)
                .executeUpdate();
    }

    @Override
    public Optional<PasswordReset> getExpDateByResetToken(String passwordResetToken) {
        try {
            Query query = em.createNativeQuery("select expiry_date from passwordreset where token = ?", "GetExpDateByToken")
                    .setParameter(1, passwordResetToken);
            return Optional.of((PasswordReset) query.getSingleResult());
        }catch (NoResultException n){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> getCredentialIdByPasswordResetToken(String passwordResetToken) {
        try {
            Query query = em.createNativeQuery("select credential_id from credential" +
                    " inner join passwordreset p" +
                    " on credential.credential_id = p.fk_passwordreset_credential_id " +
                    " where token = ?")
                    .setParameter(1, passwordResetToken);
            BigInteger result = (BigInteger) query.getSingleResult();
            return Optional.of((Long) result.longValue());
        }catch(NoResultException n){
            return  Optional.empty();
        }
    }
}
