package com.example.PushCash.repositories;

import com.example.PushCash.models.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,Long> {
    List<ServiceProvider> findByServiceType(String serviceType);
}
