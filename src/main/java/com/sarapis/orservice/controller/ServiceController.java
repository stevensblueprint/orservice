package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.service.ServiceService;
import java.util.List;
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

@RestController
@RequestMapping("/api/services")
public class ServiceController {
  private final ServiceService serviceService;

  @Autowired
  public ServiceController(ServiceService service) {
    this.serviceService = service;
  }

  @GetMapping
  public ResponseEntity<PaginationDTO<ServiceDTO>> getAllServices(@RequestParam(defaultValue = "1") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer perPage) {
    List<ServiceDTO> services = serviceService.getAllServices();
    PaginationDTO<ServiceDTO> paginationDTO = PaginationDTO.of(
        services,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
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
