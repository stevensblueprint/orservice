package com.sarapis.orservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Returned response for a service at location entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#service-at-location">Reference</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAtLocationDTO {
    private String id;
    private String serviceId;
    private String locationId;
    private String description;
    private List<ServiceAreaDTO> serviceAreas = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<ScheduleDTO> schedules = new ArrayList<>();
    private LocationDTO location;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();
}
