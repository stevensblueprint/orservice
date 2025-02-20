package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
            .map(Location::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public LocationDTO getLocationById(String id) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        return location.toDTO();
    }

    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        if (locationDTO.getId() == null || locationDTO.getId().isEmpty()) {
            locationDTO.setId(UUID.randomUUID().toString());
        }
        logger.info(locationDTO.toString());
        Location location = locationDTO.toEntity();
        Location saved = locationRepository.save(location);
        logger.info(saved.toString());
        return saved.toDTO();
    }

    @Override
    public LocationDTO updateLocation(String id, LocationDTO locationDTO) {
        Location existing = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        existing.setLocationType(locationDTO.getLocationType());
        existing.setUrl(locationDTO.getUrl());
        existing.setName(locationDTO.getName());
        existing.setAlternateName(locationDTO.getAlternateName());
        existing.setDescription(locationDTO.getDescription());
        existing.setTransportation(locationDTO.getTransportation());
        existing.setLatitude(locationDTO.getLatitude());
        existing.setLongitude(locationDTO.getLongitude());
        existing.setExternalIdentifier(locationDTO.getExternalIdentifier());
        existing.setExternalIdentifierType(locationDTO.getExternalIdentifierType());
        Location updated = locationRepository.save(existing);
        return updated.toDTO();
    }

    @Override
    public void deleteLocation(String id) {
        Location existing = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        locationRepository.delete(existing);
    }
}
