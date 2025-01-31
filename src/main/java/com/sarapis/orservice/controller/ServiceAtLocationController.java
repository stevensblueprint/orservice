package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceAtLocationDTO;
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
  public ResponseEntity<PaginationDTO<ServiceAtLocationDTO>> getAllServiceAtLocations(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                                      @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
    List<ServiceAtLocationDTO> servLocDTOs = this.serviceAtLocationService.getAllServicesAtLocation();

    try {
      PaginationDTO<ServiceAtLocationDTO> paginationDTO = PaginationDTO.of(
              servLocDTOs,
              page,
              perPage
      );
      return ResponseEntity.ok(paginationDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

    @GetMapping("/{serviceAtLocationId}")
    public ResponseEntity<ServiceAtLocationDTO> getServiceAtLocationById(@PathVariable String serviceAtLocationId) {
        ServiceAtLocationDTO serviceAtLocation = this.serviceAtLocationService
                .getServiceAtLocationById(serviceAtLocationId);
        return ResponseEntity.ok(serviceAtLocation);
    }

    @PostMapping
    public ResponseEntity<ServiceAtLocationDTO> createServiceAtLocation(
            @RequestBody UpsertServiceAtLocationDTO upsertServiceAtLocationDTO) {
        ServiceAtLocationDTO createdServiceAtLocation = this.serviceAtLocationService
                .createServiceAtLocation(upsertServiceAtLocationDTO);
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
