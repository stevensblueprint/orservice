package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.exception.InvalidInputException;
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
    List<ServiceAtLocationDTO> servLocDTOs = this.serviceAtLocationService.getAllServiceAtLocations();
    if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
    if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");
    PaginationDTO<ServiceAtLocationDTO> paginationDTO = PaginationDTO.of(
        servLocDTOs,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO> getServiceAtLocationById(@PathVariable String id) {
    ServiceAtLocationDTO dto = serviceAtLocationService.getServiceAtLocationById(id);
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<ServiceAtLocationDTO> createServiceAtLocation(@RequestBody ServiceAtLocationDTO dto) {
    ServiceAtLocationDTO created = serviceAtLocationService.createServiceAtLocation(dto);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO> updateServiceAtLocation(@PathVariable String id,
      @RequestBody ServiceAtLocationDTO dto) {
    ServiceAtLocationDTO updated = serviceAtLocationService.updateServiceAtLocation(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteServiceAtLocation(@PathVariable String id) {
    serviceAtLocationService.deleteServiceAtLocation(id);
    return ResponseEntity.noContent().build();
  }

}
