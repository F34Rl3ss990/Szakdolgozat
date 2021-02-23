package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.interfaces.CredentialRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class CredentialRepositoryImpl implements CredentialRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Credential> findByEmail(String email) {
        try{
            StoredProcedureQuery query = em.createStoredProcedureQuery("CREDENTIAL_FIND_BY_EMAIL",  "FindByEmailMapping");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, email);
            query.execute();
            return Optional.of((Credential) query.getSingleResult());
        }catch(NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("CREDENTIAL_EXISTS_BY_EMAIL", "ExistByEmailMapping");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, email);
            query.execute();
            Optional<Credential> credential = Optional.of((Credential) query.getSingleResult());
            if(credential!= null)
                return true;
            else
                return false;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public void changeEmail(String email, Long credentialId){
        em.createNativeQuery("update credential c" +
                " set c.email = ? " +
                "where c.credential_id = ?")
                .setParameter(1, email)
                .setParameter(2, credentialId)
                .executeUpdate();
    }

    @Override
    public Optional<Credential> findById (Long credentialId){
        try{
            StoredProcedureQuery query = em.createStoredProcedureQuery("CREDENTIAL_FIND_BY_ID", "ExistByEmailMapping");
                    query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
                    query.setParameter(1, credentialId);
            query.execute();
            return Optional.of((Credential) query.getResultList());
        } catch(NoResultException e){
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void saveAdmin(Credential credential) {
        em.createNativeQuery("INSERT into credential (email, password, multifactor_auth, permission, secret)" +
                "values (?, ?, ?, ?, ?)")
                .setParameter(1, credential.getEmail())
                .setParameter(2, credential.getPassword())
                .setParameter(3, credential.getMfa())
                .setParameter(4, credential.getPermission())
                .setParameter(5, credential.getSecret())
                .executeUpdate();
    }

    @Override
    @Transactional
    public Long saveUser(Credential credential) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("CREDENTIAL_SAVE_USER");
                query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter(3, Long.class, ParameterMode.OUT);
                query.setParameter(1, credential.getEmail());
                query.setParameter(2, credential.getPassword());
        query.execute();
        return (Long) query.getOutputParameterValue(3);
    }


    @Override
    public String getMultiFactorAuth(String email){
            StoredProcedureQuery query = em.createStoredProcedureQuery("CREDENTIAL_GET_MFA");
                    query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
                    query.setParameter(1, email);
            query.execute();
            return (String) query.getSingleResult();
    }

    @Transactional
    @Override
    public void changePassword(final Long credentialId, final String password) {
        em.createNativeQuery("UPDATE credential" +
                " set password = ?" +
                " where credential_id = ?")
                .setParameter(1, password)
                .setParameter(2, credentialId)
                .executeUpdate();
    }

    @Override
    public Optional<Credential> findByEmailForMFA(String email){
        try {
            Query query = em.createNativeQuery("SELECT credential_id, email, permission, password, multifactor_auth, secret" +
                    " from credential" +
                    " where email = ?","GetCredentialForMultiFactorAuth" )
                    .setParameter(1, email);
            return Optional.of((Credential) query.getSingleResult());
        }catch(NoResultException n){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Credential> getByEmail(String email){
        try {
            Query query = em.createNativeQuery("SELECT email, credential_id" +
                    " from credential" +
                    " where email = ?", "GetEmailAndId")
                    .setParameter(1, email);
            return Optional.of((Credential) query.getSingleResult());
        }catch(NoResultException n){
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void setPermissionOnVerifiedUser(Long credentialId) {
        em.createNativeQuery("UPDATE credential" +
                " SET permission = ?" +
                " where credential_id = ? ")
                .setParameter(1, "ROLE_USER")
                .setParameter(2, credentialId)
                .executeUpdate();
    }

    @Override
    public Optional<Credential> getPasswordAndIdByEmail(String email){
        try {
            Query query = em.createNativeQuery("SELECT credential_id, password" +
                    " from credential" +
                    " where email = ?","GetPasswordAndId")
                    .setParameter(1, email);
            return Optional.of((Credential) query.getSingleResult());
        }catch(NoResultException n){
            return Optional.empty();
        }
    }

    @Override
    public String getPermissionById(Long verificationFkId){
        Query query = em.createNativeQuery("SELECT permission" +
                " from credential" +
                " where credential_id = ?")
                .setParameter(1, verificationFkId);
        return (String) query.getSingleResult();
    }

    @Transactional
    @Override
    public void disableUserAccountByAdmin(Long userid) {
        em.createNativeQuery("update credential" +
                " inner join user u" +
                " on credential.credential_id = u.fk_user_credential" +
                " set permission = 'ROLE_BANNED'" +
                " where user_id = ?")
                .setParameter(1, userid)
                .executeUpdate();
    }

    @Transactional
    @Override
    public void disableAccountByUser(Long credentialId) {
        em.createNativeQuery("update credential set permission = 'ROLE_SELFDISABLE' where credential_id = ?")
                .setParameter(1, credentialId)
                .executeUpdate();
    }

    @Override
    public List<Credential> getAllAdmin() {
        Query query = em.createNativeQuery("SELECT credential_id, email, first_name, last_name from credential where permission = 'ROLE_ADMIN' ");
        return (List<Credential>) query.getResultList();
    }
}
