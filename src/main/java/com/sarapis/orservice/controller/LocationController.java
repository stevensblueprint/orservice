package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PaginationDTO<LocationDTO>> getAllLocations(@RequestParam(required = false) String search) {
        List<LocationDTO> locations = this.locationService.getAllLocations(search);
        PaginationDTO<LocationDTO> paginationDTO = PaginationDTO.of(
                locations.size(),
                1,
                1,
                locations.size(),
                true,
                false,
                false,
                locations
        );
        return ResponseEntity.ok(paginationDTO);
    }
}
