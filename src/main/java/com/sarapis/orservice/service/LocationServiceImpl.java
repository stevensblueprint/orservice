package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final OrganizationRepository organizationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository,
                               OrganizationRepository organizationRepository,
                               AttributeService attributeService,
                               MetadataService metadataService) {
        this.locationRepository = locationRepository;
        this.organizationRepository = organizationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        List<LocationDTO> locationDTOs = this.locationRepository.findAll()
                .stream().map(Location::toDTO).toList();
        locationDTOs.forEach(this::addRelatedData);
        return locationDTOs;
    }

    @Override
    public LocationDTO getLocationById(String locationId) {
        Location location = this.locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found."));
        LocationDTO locationDTO = location.toDTO();
        this.addRelatedData(locationDTO);
        return locationDTO;
    }

    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        Organization organization = null;

        if (locationDTO.getOrganizationId() != null) {
            organization = this.organizationRepository.findById(locationDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
        }

        Location location = locationDTO.toEntity(organization);
        locationDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(location.getId(), attributeDTO));
        locationDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(locationDTO.getId(), e));

        Location createdLocation = this.locationRepository.save(location);
        return this.getLocationById(createdLocation.getId());
    }

    @Override
    public LocationDTO updateLocation(String locationId, LocationDTO locationDTO) {
        Location location = this.locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found."));

        location.setLocationType(locationDTO.getLocationType());
        location.setUrl(locationDTO.getUrl());
        location.setName(locationDTO.getName());
        location.setAlternateName(locationDTO.getAlternateName());
        location.setDescription(locationDTO.getDescription());
        location.setTransportation(locationDTO.getTransportation());
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setExternalIdentifier(locationDTO.getExternalIdentifier());
        location.setExternalIdentifierType(locationDTO.getExternalIdentifierType());

        Location updatedLocation = this.locationRepository.save(location);
        return this.getLocationById(updatedLocation.getId());
    }

    @Override
    public void deleteLocation(String locationId) {
        Location location = this.locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found."));
        this.attributeService.deleteRelatedAttributes(location.getId());
        this.metadataService.deleteRelatedMetadata(location.getId());
        this.locationRepository.delete(location);
    }

    private void addRelatedData(LocationDTO locationDTO) {
        locationDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(locationDTO.getId()));
        locationDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(locationDTO.getId()));
    }
}
