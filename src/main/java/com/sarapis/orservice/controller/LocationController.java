package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.upsert.UpsertLocationDTO;
import com.sarapis.orservice.exception.InvalidInputException;
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
    public ResponseEntity<PaginationDTO<LocationDTO>> getAllLocations(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                      @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
        List<LocationDTO> locations = this.locationService.getAllLocations();

        if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
        if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

        PaginationDTO<LocationDTO> pagination = PaginationDTO.of(
            locations,
            page,
            perPage
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
