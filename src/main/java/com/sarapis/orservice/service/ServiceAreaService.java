package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAreaDTO;

import java.util.List;

public interface ServiceAreaService {
    List<ServiceAreaDTO> getAllServiceAreas();

    ServiceAreaDTO getServiceAreaById(String serviceAreaId);

    ServiceAreaDTO createServiceArea(ServiceAreaDTO serviceAreaDTO);

    ServiceAreaDTO updateServiceArea(String serviceAreaId, ServiceAreaDTO serviceAreaDTO);

    void deleteServiceArea(String serviceAreaId);
}
