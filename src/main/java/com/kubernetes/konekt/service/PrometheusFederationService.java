package com.kubernetes.konekt.service;

import com.kubernetes.konekt.entity.PrometheusFederation;

public interface PrometheusFederationService {
    public PrometheusFederation getPrometheusFederationById(Long id);
    public void savePrometheusFederation(PrometheusFederation prometheusFederation);

}
