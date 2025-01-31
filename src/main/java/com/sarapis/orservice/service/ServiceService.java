package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceDTO;

import java.util.List;

public interface ServiceService {
    List<ServiceDTO> getAllServices();

    ServiceDTO getServiceById(String serviceId);

    ServiceDTO createService(UpsertServiceDTO upsertServiceDTO);

    ServiceDTO updateService(String serviceId, ServiceDTO serviceDTO);

    void deleteService(String serviceId);
}
