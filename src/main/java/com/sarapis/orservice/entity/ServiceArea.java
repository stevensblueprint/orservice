package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "service_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceArea {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "service_at_location_id")
    private ServiceAtLocation serviceAtLocation;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "extent")
    private String extent;

    @Enumerated(EnumType.STRING)
    @Column(name = "extent_type")
    private ExtentType extentType;

    @Column(name = "uri")
    private String uri;

    public ServiceAreaDTO toDTO() {
        return ServiceAreaDTO.builder()
                .id(this.id)
                .serviceId(this.service == null ? null : this.service.getId())
                .serviceAtLocationId(this.serviceAtLocation == null ? null : this.serviceAtLocation.getId())
                .name(this.name)
                .description(this.description)
                .extent(this.extent)
                .extentType(this.extentType)
                .uri(this.uri)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
