package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ServiceCapacityDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "unit_id")
    private Unit unit = null;

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
                .unit(this.unit != null ? this.unit.toDTO() : null)
                .available(this.available)
                .maximum(this.maximum)
                .description(this.description)
                .updated(this.updated)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
