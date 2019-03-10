package com.kubernetes.konekt.dao;

import com.kubernetes.konekt.entity.PrometheusFederation;

public interface PrometheusFederationDao {
    public PrometheusFederation getPrometheusFederationById(Long id);
    public void savePrometheusFederation(PrometheusFederation prometheusFederation);
}
