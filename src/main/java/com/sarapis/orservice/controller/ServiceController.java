package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceDTO;
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

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable String serviceId) {
        ServiceDTO service = this.serviceService.getServiceById(serviceId);
        return ResponseEntity.ok(service);
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@RequestBody UpsertServiceDTO upsertServiceDTO) {
        ServiceDTO createdService = this.serviceService.createService(upsertServiceDTO);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable String serviceId,
                                                    @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO updatedService = this.serviceService.updateService(serviceId, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable String serviceId) {
        this.serviceService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
}
