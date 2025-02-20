package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocations();
    LocationDTO getLocationById(String id);
    LocationDTO createLocation(LocationDTO locationDTO);
    LocationDTO updateLocation(String id, LocationDTO locationDTO);
    void deleteLocation(String id);
}
