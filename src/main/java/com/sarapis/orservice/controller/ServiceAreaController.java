package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.service.ServiceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{serviceAreaId}")
    public ResponseEntity<ServiceAreaDTO> getServiceAreaById(@PathVariable String serviceAreaId) {
        ServiceAreaDTO serviceArea = this.serviceAreaService.getServiceAreaById(serviceAreaId);
        return ResponseEntity.ok(serviceArea);
    }

    @PostMapping
    public ResponseEntity<ServiceAreaDTO> createServiceArea(@RequestBody ServiceAreaDTO serviceAreaDTO) {
        ServiceAreaDTO createdServiceArea = this.serviceAreaService.createServiceArea(serviceAreaDTO);
        return ResponseEntity.ok(createdServiceArea);
    }

    @PutMapping("/{serviceAreaId}")
    public ResponseEntity<ServiceAreaDTO> updateServiceArea(@PathVariable String serviceAreaId,
                                                            @RequestBody ServiceAreaDTO serviceAreaDTO) {
        ServiceAreaDTO updatedServiceArea = this.serviceAreaService.updateServiceArea(serviceAreaId, serviceAreaDTO);
        return ResponseEntity.ok(updatedServiceArea);
    }

    @DeleteMapping("/{serviceAreaId}")
    public ResponseEntity<Void> deleteServiceArea(@PathVariable String serviceAreaId) {
        this.serviceAreaService.deleteServiceArea(serviceAreaId);
        return ResponseEntity.noContent().build();
    }

}
