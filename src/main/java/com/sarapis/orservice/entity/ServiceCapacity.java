package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "service_capacity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCapacity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @OneToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "available")
    private int available;

    @Column(name = "maximum")
    private int maximum;

    @Column(name = "description")
    private String description;

    @Column(name = "updated")
    private LocalDate updated;

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes;

    @OneToMany
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata;
}
