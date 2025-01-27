package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.service.ServiceAtLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service_at_locations")
public class ServiceAtLocationController {
    private final ServiceAtLocationService serviceAtLocationService;

    @Autowired
    public ServiceAtLocationController(ServiceAtLocationService serviceAtLocationService) {
        this.serviceAtLocationService = serviceAtLocationService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<ServiceAtLocationDTO>> getAllServiceAtLocations() {
        List<ServiceAtLocationDTO> servicesAtLocations = this.serviceAtLocationService.getAllServicesAtLocation();
        PaginationDTO<ServiceAtLocationDTO> pagination = PaginationDTO.of(
                servicesAtLocations.size(),
                1,
                1,
                servicesAtLocations.size(),
                true,
                false,
                false,
                servicesAtLocations
        );
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/{serviceAtLocationId}")
    public ResponseEntity<ServiceAtLocationDTO> getServiceAtLocationById(@PathVariable String serviceAtLocationId) {
        ServiceAtLocationDTO serviceAtLocation = this.serviceAtLocationService
                .getServiceAtLocationById(serviceAtLocationId);
        return ResponseEntity.ok(serviceAtLocation);
    }

    @PostMapping
    public ResponseEntity<ServiceAtLocationDTO> createServiceAtLocation(
            @RequestBody ServiceAtLocationDTO serviceAtLocationDTO) {
        ServiceAtLocationDTO createdServiceAtLocation = this.serviceAtLocationService
                .createServiceAtLocation(serviceAtLocationDTO);
        return ResponseEntity.ok(createdServiceAtLocation);
    }

    @PutMapping("/{serviceAtLocationId}")
    public ResponseEntity<ServiceAtLocationDTO> updateServiceAtLocation(@PathVariable String serviceAtLocationId,
                                                                        @RequestBody ServiceAtLocationDTO serviceAtLocationDTO) {
        ServiceAtLocationDTO updatedServiceAtLocation = this.serviceAtLocationService
                .updateServiceAtLocation(serviceAtLocationId, serviceAtLocationDTO);
        return ResponseEntity.ok(updatedServiceAtLocation);
    }

    @DeleteMapping("/{serviceAtLocationId}")
    public ResponseEntity<Void> deleteServiceAtLocation(@PathVariable String serviceAtLocationId) {
        this.serviceAtLocationService.deleteServiceAtLocation(serviceAtLocationId);
        return ResponseEntity.noContent().build();
    }

}
