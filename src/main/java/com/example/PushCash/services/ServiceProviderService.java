package com.example.PushCash.services;

import com.example.PushCash.models.ServiceProvider;
import com.example.PushCash.repositories.ServiceProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProviderService {
    ServiceProviderRepository serviceProviderRepository;
    ServiceProviderService(ServiceProviderRepository serviceProviderRepository){
        this.serviceProviderRepository=serviceProviderRepository;
    }
    public List<ServiceProvider>findByServiceType(String serviceType){
        return serviceProviderRepository.findByServiceType(serviceType);
    }
    public Optional<ServiceProvider> findById(Long providerId){
        return serviceProviderRepository.findById(providerId);
    }
    public List<ServiceProvider>findAll(){
        return serviceProviderRepository.findAll();
    }
}
