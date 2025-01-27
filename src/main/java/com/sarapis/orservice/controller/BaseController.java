package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.*;
import com.sarapis.orservice.service.LocationService;
import com.sarapis.orservice.service.OrganizationService;
import com.sarapis.orservice.service.ServiceAtLocationService;
import com.sarapis.orservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class BaseController {
    private final OrganizationService organizationService;
    private final ServiceService serviceService;
    private final LocationService locationService;
    private final ServiceAtLocationService serviceAtLocationService;

    @Autowired
    public BaseController(OrganizationService organizationService, ServiceService serviceService, LocationService locationService, ServiceAtLocationService serviceAtLocationService) {
        this.organizationService = organizationService;
        this.serviceService = serviceService;
        this.locationService = locationService;
        this.serviceAtLocationService = serviceAtLocationService;
    }

    @GetMapping
    public ResponseEntity<RootDTO> getRoot() {
        RootDTO root = RootDTO.builder()
                .version("3.0")
                .profile("https://docs.openreferraluk.org/en/latest/")
                .openapiUrl("https://raw.githubusercontent.com/openreferral/specification/3.0/schema/openapi.json")
                .build();
        return ResponseEntity.ok(root);
    }

    @GetMapping("/search")
    public ResponseEntity<FullTextSearchDTO> fullTextSearch(@RequestParam(required = false) String search) {
        List<OrganizationDTO> organizationDTOs = this.organizationService.getAllOrganizations(search);
        List<ServiceDTO> serviceDTOs = this.serviceService.getAllServices(search);
        List<LocationDTO> locationDTOs = this.locationService.getAllLocations(search);
        List<ServiceAtLocationDTO> serviceAtLocationDTOs = this.serviceAtLocationService.getAllServicesAtLocations(search);

        FullTextSearchDTO fullTextSearchDTO = FullTextSearchDTO.builder()
                .organizations(organizationDTOs)
                .services(serviceDTOs)
                .locations(locationDTOs)
                .serviceAtLocations(serviceAtLocationDTOs)
                .build();

        return ResponseEntity.ok(fullTextSearchDTO);
    }
}
