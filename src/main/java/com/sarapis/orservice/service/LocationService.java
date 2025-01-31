package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertLocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocations();

    LocationDTO getLocationById(String locationId);

    LocationDTO createLocation(UpsertLocationDTO upsertLocationDTO);

    LocationDTO updateLocation(String locationId, UpsertLocationDTO upsertLocationDTO);

    void deleteLocation(String locationId);
}
