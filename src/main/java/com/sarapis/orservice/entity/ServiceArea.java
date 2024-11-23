package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceArea {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

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

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();

    public ServiceAreaDTO toDTO() {
        return ServiceAreaDTO.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .extent(this.extent)
                .extentType(this.extentType)
                .uri(this.uri)
                .attributes(this.attributes.stream().map(Attribute::toDTO).toList())
                .metadata(this.metadata.stream().map(Metadata::toDTO).toList())
                .build();
    }
}
