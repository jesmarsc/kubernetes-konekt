package com.kubernetes.konekt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.PrometheusFederationDao;
import com.kubernetes.konekt.entity.PrometheusFederation;

@Service
public class PrometheusFederationServiceImpl implements PrometheusFederationService {
    @Autowired
    private PrometheusFederationDao prometheusFederationDao;
    
    @Override
    @Transactional
    public PrometheusFederation getPrometheusFederationById(Long id) {
        return prometheusFederationDao.getPrometheusFederationById(id);
      
    }

    @Override
    @Transactional
    public void savePrometheusFederation(PrometheusFederation prometheusFederation) {
        prometheusFederationDao.savePrometheusFederation(prometheusFederation);
        
    }

}
