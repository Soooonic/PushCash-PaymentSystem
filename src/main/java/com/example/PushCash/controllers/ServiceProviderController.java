package com.example.PushCash.controllers;

import com.example.PushCash.models.ServiceProvider;
import com.example.PushCash.services.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ServiceProviderController {
    ServiceProviderService serviceProviderService;
    @Autowired
    ServiceProviderController(ServiceProviderService serviceProviderService){
        this.serviceProviderService=serviceProviderService;
    }
    @GetMapping("/providers")
    public List<Map<String,Object>> getProviders(@RequestParam("type") String serviceType){
        List<ServiceProvider>providers= serviceProviderService.findByServiceType(serviceType);
        List<Map<String,Object>>list=new ArrayList<>();
        for (ServiceProvider provider:
             providers) {
            Map<String,Object>providerMap=new HashMap<>();
            providerMap.put("id", provider.getProviderId());
            providerMap.put("name", provider.getProviderName());
            providerMap.put("price", provider.getPrice());
            list.add(providerMap);
        }
        return list;
    }
}
