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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<PaginationDTO<ServiceAtLocationDTO>> getAllServiceAtLocations(@RequestParam(defaultValue = "1") Integer page,
                                                                                      @RequestParam(defaultValue = "10") Integer perPage) {
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

  @GetMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO> getServiceAtLocationById(@PathVariable String id) {
    ServiceAtLocationDTO servLocDTO = this.serviceAtLocationService.getServiceAtLocationById(id);
    return ResponseEntity.ok(servLocDTO);
  }

  @PostMapping
  public ResponseEntity<ServiceAtLocationDTO> createServiceAtLocation(@RequestBody ServiceAtLocationDTO serviceAtLocationDTO) {
    ServiceAtLocationDTO createdServLocDTO = this.serviceAtLocationService.createServiceAtLocation(serviceAtLocationDTO);
    return ResponseEntity.ok(createdServLocDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO> updateServiceAtLocation(@PathVariable String id, @RequestBody ServiceAtLocationDTO serviceAtLocationDTO) {
    ServiceAtLocationDTO updatedServLocDTO = this.serviceAtLocationService.updateServiceAtLocation(id, serviceAtLocationDTO);
    return ResponseEntity.ok(updatedServLocDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteServiceAtLocation(@PathVariable String id) {
    serviceAtLocationService.deleteServiceAtLocation(id);
    return ResponseEntity.noContent().build();
  }

}
