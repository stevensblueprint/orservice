package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public ServiceAtLocation toEntity(Service service, Location location) {
        ServiceAtLocation serviceAtLocation = ServiceAtLocation.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .location(location)
                .description(this.description)
                .build();
        serviceAtLocation.setServiceAreas(this.serviceAreas.stream().map(e -> e.toEntity(null, serviceAtLocation)).toList());
        serviceAtLocation.setContacts(this.contacts.stream().map(e -> e.toEntity(null, null, serviceAtLocation, null)).toList());
        serviceAtLocation.setPhones(this.phones.stream().map(e -> e.toEntity(null, null, null, null, serviceAtLocation)).toList());
        serviceAtLocation.setSchedules(this.schedules.stream().map(e -> e.toEntity(null, null, serviceAtLocation)).toList());
        return serviceAtLocation;
    }
}
