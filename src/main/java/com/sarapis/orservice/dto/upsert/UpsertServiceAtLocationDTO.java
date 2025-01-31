package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The request body when inserting or updating a service at location entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertServiceAtLocationDTO {
    private String description;
    private String serviceId;
    private String locationId;
    private List<String> serviceAreas;
    private List<String> contacts;
    private List<String> phones;
    private List<String> schedules;

    public ServiceAtLocation create() {
        return ServiceAtLocation.builder()
                .id(UUID.randomUUID().toString())
                .description(description)
                .build();
    }

    public ServiceAtLocation merge(ServiceAtLocation serviceAtLocation) {
        return ServiceAtLocation.builder()
                .id(serviceAtLocation.getId())
                .description(description == null ? serviceAtLocation.getDescription() : description)
                .service(serviceAtLocation.getService())
                .location(serviceAtLocation.getLocation())
                .serviceAreas(serviceAtLocation.getServiceAreas())
                .contacts(serviceAtLocation.getContacts())
                .phones(serviceAtLocation.getPhones())
                .schedules(serviceAtLocation.getSchedules())
                .build();
    }

    public static ServiceAtLocation create(Service service, Location location) {
        return ServiceAtLocation.builder()
                .id(UUID.randomUUID().toString())
                .service(service)
                .location(location)
                .serviceAreas(new ArrayList<>())
                .contacts(new ArrayList<>())
                .phones(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();
    }
}
