package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAtLocationDTO {
    private String id;
    private String serviceId;
    private String locationId;
    private String description;

    // Related entities represented as DTOs
    private List<ServiceAreaDTO> serviceAreas;
    private List<ContactDTO> contacts;
    private List<PhoneDTO> phones;
    private List<ScheduleDTO> schedules;

    // In case you want to expose the full location details
    private LocationDTO location;

    // Additional fields for flexibility (attributes, metadata)
    private List<Object> attributes;
    private List<Object> metadata;
}
