package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import java.util.List;

public interface ServiceService {
    List<ServiceDTO> getAllServices();

    ServiceDTO getServiceById(String serviceId);

    ServiceDTO createService(ServiceDTO serviceDTO);

    ServiceDTO updateService(String serviceId, ServiceDTO serviceDTO);

    void deleteService(String serviceId);
}
