package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
  private final ServiceService serviceService;

  @Autowired
  public ServiceController(ServiceService serviceService) {
    this.serviceService = serviceService;
  }

  @GetMapping
  public ResponseEntity<PaginationDTO<ServiceDTO>> getAllServices() {
    return null;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceDTO> getServiceById(@PathVariable String id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceDTO> updateService(@PathVariable String id, @RequestBody ServiceDTO serviceDTO) {
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteService(@PathVariable String id) {
    serviceService.deleteService(id);
    return ResponseEntity.noContent().build();
  }
}
