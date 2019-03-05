package com.kubernetes.konekt.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.PrometheusFederation;

@Repository
public class PrometheusFederationDaoImpl implements PrometheusFederationDao {

    @Autowired
    private EntityManager factory;
    
    @Override
    public PrometheusFederation getPrometheusFederationById(Long id) {
        Session currentSession = factory.unwrap(Session.class);

        Query<PrometheusFederation> theQuery = currentSession.createQuery("from PrometheusFederation where id=:id", PrometheusFederation.class);
        theQuery.setParameter("id", id);
        PrometheusFederation prometheusFederation = null;
        
        try {
            prometheusFederation = theQuery.getSingleResult();
        } catch (Exception e) {
            prometheusFederation = null;
        }
        return prometheusFederation;
    }
    
    @Override
    public void savePrometheusFederation(PrometheusFederation prometheusFederation) {

        Session currentSession = factory.unwrap(Session.class);    
        currentSession.saveOrUpdate(prometheusFederation);

    }

}
