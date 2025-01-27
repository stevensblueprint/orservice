package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ServiceCapacityDTO;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "service_capacity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCapacity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "available", nullable = false)
    private int available;

    @Column(name = "maximum")
    private int maximum;

    @Column(name = "description")
    private String description;

    @Column(name = "updated", nullable = false)
    private LocalDate updated;

    public ServiceCapacityDTO toDTO() {
        return ServiceCapacityDTO.builder()
                .id(this.id)
                .serviceId(this.service.getId())
                .unitId(this.unit.getId())
                .available(this.available)
                .maximum(this.maximum)
                .description(this.description)
                .updated(this.updated)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
