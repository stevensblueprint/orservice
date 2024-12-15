package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.locationRepository = locationRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }


    @Override
    public List<LocationDTO> getAllLocations(String search) {
        List<LocationDTO> locationDTOs = this.locationRepository.getAllLocations(search).stream().map(Location::toDTO).toList();
        locationDTOs.forEach(this::addRelatedData);
        return locationDTOs;
    }

    @Override
    public LocationDTO getLocationById(String id) {
        return null;
    }

    @Override
    public LocationDTO createLocation(LocationDTO location) {
        return null;
    }

    @Override
    public LocationDTO updateLocation(String id, LocationDTO location) {
        return null;
    }

    @Override
    public void deleteLocation(String id) {

    }

    private void addRelatedData(LocationDTO locationDTO) {
        locationDTO.getAttributes().addAll(this.locationRepository.getAttributes(locationDTO.getId()).stream().map(Attribute::toDTO).toList());
        locationDTO.getMetadata().addAll(this.locationRepository.getMetadata(locationDTO.getId()).stream().map(Metadata::toDTO).toList());
    }
}
