package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.service.ServiceAtLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return null;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO> getServiceAtLocationById(@PathVariable String id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<ServiceAtLocationDTO> createServiceAtLocation(@RequestBody ServiceAtLocationDTO serviceAtLocationDTO) {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO> updateServiceAtLocation(@PathVariable String id, @RequestBody ServiceAtLocationDTO serviceAtLocationDTO) {
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteServiceAtLocation(@PathVariable String id) {
    serviceAtLocationService.deleteServiceAtLocation(id);
    return ResponseEntity.noContent().build();
  }

}
