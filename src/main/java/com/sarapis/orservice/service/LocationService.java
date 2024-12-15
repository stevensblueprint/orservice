package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocations(String search);

    LocationDTO getLocationById(String id);

    LocationDTO createLocation(LocationDTO location);

    LocationDTO updateLocation(String id, LocationDTO location);

    void deleteLocation(String id);
}
