package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.upsert.UpsertLocationDTO;
import com.sarapis.orservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<LocationDTO>> getAllLocations() {
        List<LocationDTO> locations = this.locationService.getAllLocations();
        PaginationDTO<LocationDTO> pagination = PaginationDTO.of(
                locations.size(),
                1,
                1,
                locations.size(),
                true,
                false,
                false,
                locations
        );
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable String locationId) {
        LocationDTO location = this.locationService.getLocationById(locationId);
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody UpsertLocationDTO upsertLocationDTO) {
        LocationDTO createdLocation = this.locationService.createLocation(upsertLocationDTO);
        return ResponseEntity.ok(createdLocation);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable String locationId,
                                                      @RequestBody LocationDTO locationDTO) {
        LocationDTO updatedLocation = this.locationService.updateLocation(locationId, locationDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String locationId) {
        this.locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }
}
