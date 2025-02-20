package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.exception.InvalidInputException;
import com.sarapis.orservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService service) {
        this.serviceService = service;
    }

  @GetMapping
  public ResponseEntity<PaginationDTO<ServiceDTO>> getAllServices(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                  @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
    List<ServiceDTO> services = serviceService.getAllServices();

    if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
    if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

    PaginationDTO<ServiceDTO> paginationDTO = PaginationDTO.of(
        services,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceDTO> getServiceById(@PathVariable String id) {
    ServiceDTO serviceDTO = serviceService.getServiceById(id);
    return ResponseEntity.ok(serviceDTO);
  }

  @PostMapping
  public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
    ServiceDTO created = serviceService.createService(serviceDTO);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceDTO> updateService(@PathVariable String id, @RequestBody ServiceDTO serviceDTO) {
    ServiceDTO updated = serviceService.updateService(id, serviceDTO);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteService(@PathVariable String id) {
    serviceService.deleteService(id);
    return ResponseEntity.noContent().build();
  }
}
