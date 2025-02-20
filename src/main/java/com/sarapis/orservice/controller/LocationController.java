package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable String id) {
        LocationDTO locationDTO = locationService.getLocationById(id);
        return ResponseEntity.ok(locationDTO);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        LocationDTO created = locationService.createLocation(locationDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable String id, @RequestBody LocationDTO locationDTO) {
        LocationDTO updated = locationService.updateLocation(id, locationDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
