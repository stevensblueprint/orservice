package com.sarapis.orservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullTextSearchDTO {
    private List<OrganizationDTO> organizations;
    private List<ServiceDTO> services;
    private List<LocationDTO> locations;
    private List<ServiceAtLocationDTO> serviceAtLocations;
}
