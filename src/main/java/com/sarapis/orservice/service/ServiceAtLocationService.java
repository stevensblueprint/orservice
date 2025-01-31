package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceAtLocationDTO;

import java.util.List;

public interface ServiceAtLocationService {
    List<ServiceAtLocationDTO> getAllServicesAtLocation();

    ServiceAtLocationDTO getServiceAtLocationById(String serviceAtLocationId);

    ServiceAtLocationDTO createServiceAtLocation(UpsertServiceAtLocationDTO upsertServiceAtLocationDTO);

    ServiceAtLocationDTO updateServiceAtLocation(String serviceAtLocationId, UpsertServiceAtLocationDTO upsertServiceAtLocationDTO);

    void deleteServiceAtLocation(String serviceAtLocationId);
}
