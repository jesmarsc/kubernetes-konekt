package com.kubernetes.konekt.entity;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prometheus_info")
public class PrometheusFederation {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name = "prometheus")
    private Blob prometheusFile;
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getPrometheusFile() {
        return prometheusFile;
    }

    public void setPrometheusFile(Blob prometheusFile) {
        this.prometheusFile = prometheusFile;
    }

    @Override
    public String toString() {
        return "PrometheusFederation [id=" + id + ", prometheusFile=" + prometheusFile + "]";
    }
    
    
    
}
