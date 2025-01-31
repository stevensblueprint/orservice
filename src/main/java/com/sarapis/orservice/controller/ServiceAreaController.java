package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.service.ServiceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service_areas")
public class ServiceAreaController {

    private final ServiceAreaService serviceAreaService;

    @Autowired
    public ServiceAreaController(ServiceAreaService serviceAreaService) {
        this.serviceAreaService = serviceAreaService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<ServiceAreaDTO>> getAllServiceAreas(@RequestParam(defaultValue = "1") Integer page,
                                                                            @RequestParam(defaultValue = "10") Integer perPage) {
        List<ServiceAreaDTO> serviceAreaDTOs = serviceAreaService.getAllServiceAreas();

        try {
            PaginationDTO<ServiceAreaDTO> paginationDTO = PaginationDTO.of(
                    serviceAreaDTOs,
                    page,
                    perPage
            );
            return ResponseEntity.ok(paginationDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceAreaDTO> getServiceAreaById(@PathVariable String id) {
        ServiceAreaDTO serviceArea = serviceAreaService.getServiceAreaById(id);
        return ResponseEntity.ok(serviceArea);
    }

    @PostMapping
    public ResponseEntity<ServiceAreaDTO> createServiceArea(@RequestBody ServiceAreaDTO serviceAreaDTO) {
        if (serviceAreaDTO.getId() == null) {
            serviceAreaDTO.setId(UUID.randomUUID().toString());
        }
        ServiceAreaDTO createdServiceArea = serviceAreaService.createServiceArea(serviceAreaDTO);
        return ResponseEntity.ok(createdServiceArea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceAreaDTO> updateServiceArea(@PathVariable String id, @RequestBody ServiceAreaDTO serviceAreaDTO) {
        ServiceAreaDTO updatedAccessibility = serviceAreaService.updateServiceArea(id, serviceAreaDTO);
        return ResponseEntity.ok(updatedAccessibility);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceArea(@PathVariable String id) {
        serviceAreaService.deleteServiceArea(id);
        return ResponseEntity.noContent().build();
    }

}
