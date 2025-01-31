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
    //================================================================================
    // Attributes
    //================================================================================

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "available", nullable = false)
    private Integer available;

    @Column(name = "maximum")
    private Integer maximum;

    @Column(name = "description")
    private String description;

    @Column(name = "updated", nullable = false)
    private LocalDate updated;

    //================================================================================
    // Relations
    //================================================================================

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    //================================================================================
    // Methods
    //================================================================================

    @PreRemove
    public void preRemove() {
        this.service.getCapacities().remove(this);
        this.unit.getServiceCapacities().remove(this);
    }

    public ServiceCapacityDTO toDTO() {
        return ServiceCapacityDTO.builder()
                .id(this.id)
                .serviceId(this.service.getId())
                .unit(this.unit.toDTO())
                .available(this.available)
                .maximum(this.maximum)
                .description(this.description)
                .updated(this.updated)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
