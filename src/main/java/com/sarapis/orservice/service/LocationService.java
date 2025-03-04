package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import java.util.List;

public interface LocationService {
  LocationDTO.Response createLocation(LocationDTO.Request dto);
  List<LocationDTO.Response> getLocationByOrganizationId(String organizationId);
}
