package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.ExtentType;
import com.sarapis.orservice.entity.ServiceArea;
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
public class ServiceAreaDTO {
    private String id;

    private String serviceId;
    private String serviceAtLocationId;

    private String name;
    private String description;
    private String extent;
    private ExtentType extentType;
    private String uri;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public ServiceArea toEntity(Service service, ServiceAtLocation serviceAtLocation) {
        return ServiceArea.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .serviceAtLocation(serviceAtLocation)
                .name(this.name)
                .description(this.description)
                .extent(this.extent)
                .extentType(this.extentType)
                .uri(this.uri)
                .build();
    }
}
