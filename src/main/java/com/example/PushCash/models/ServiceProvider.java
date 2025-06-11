package com.example.PushCash.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    private String providerName;
    private double price;
    private String serviceType;
    private String clientId;
    private String clientSecret;

    public ServiceProvider() {}

    public ServiceProvider(String providerName, double price,String serviceType,String clientId,String clientSecret) {
        this.providerName = providerName;
        this.price = price;
        this.serviceType=serviceType;
        this.clientId=clientId;
        this.clientSecret=clientSecret;
    }
    public Long getProviderId() {
        return providerId;
    }
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getServiceType() {
        return serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public boolean isAmountSufficient(double amount) {
        return amount>=price;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
