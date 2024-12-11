package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ServiceAreaService {

    List<ServiceAreaDTO> getAllServiceAreas();

    ServiceAreaDTO getServiceAreaById(String id);

    ServiceAreaDTO createServiceArea(ServiceAreaDTO serviceAreaDTO);

    ServiceAreaDTO updateServiceArea(String id, ServiceAreaDTO serviceAreaDTO);

    void deleteServiceArea(String id);
}
