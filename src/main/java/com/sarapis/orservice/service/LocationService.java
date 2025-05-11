package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;

public interface LocationService extends Exchangeable {
  LocationDTO.Response createLocation(LocationDTO.Request requestDto, String updatedBy);
}
