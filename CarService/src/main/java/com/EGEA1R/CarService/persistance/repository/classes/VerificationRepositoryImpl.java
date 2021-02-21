package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.Verification;
import com.EGEA1R.CarService.persistance.repository.interfaces.VerificationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class VerificationRepositoryImpl implements VerificationRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Verification> getFkAndExpDateByToken(String verificationToken){
        try {
            Query query = em.createNativeQuery("select fk_verification_credential_id, expiry_date from verification where token = ?", "getFkAndExpDateMapping")
                    .setParameter(1, verificationToken);
            return Optional.of((Verification) query.getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Verification> findByToken(String verificationToken) {
        try {
            Query query = em.createNativeQuery("SELECT verification_id from verification where token = ?", "GetVerificationId")
                    .setParameter(1, verificationToken);
            return Optional.of((Verification) query.getSingleResult());
        }catch(NoResultException n){
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void setNewTokenAndExpDateOnObj(Verification verification) {
        em.createNativeQuery("UPDATE verification set token = ?, expiry_date = ? where verification_id = ?")
                .setParameter(1, verification.getToken())
                .setParameter(2, verification.getExpiryDate())
                .setParameter(3, verification.getVerificationId())
                .executeUpdate();
    }

    @Override
    public String getCredentialEmailByVerificationId(Long verificationId) {
        Query query = em.createNativeQuery("select email from credential" +
                " inner join verification v" +
                " on credential.credential_id = v.fk_verification_credential_id " +
                " where verification_id = ?")
                .setParameter(1, verificationId);
        return (String) query.getSingleResult();
    }

    @Transactional
    @Override
    public void saveVerificationToken(Verification verification, Long credentialId){
        em.createNativeQuery("INSERT into verification (token, expiry_date, fk_verification_credential_id)" +
                " values (?, ?, ?)")
                .setParameter(1, verification.getToken())
                .setParameter(2, verification.getExpiryDate())
                .setParameter(3, credentialId)
                .executeUpdate();
    }
}
