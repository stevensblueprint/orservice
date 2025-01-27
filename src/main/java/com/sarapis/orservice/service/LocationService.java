package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocations();

    LocationDTO getLocation(String locationId);

    LocationDTO createLocation(LocationDTO locationDTO);

    LocationDTO updateLocation(String locationId, LocationDTO locationDTO);

    void deleteLocation(String locationId);
}
