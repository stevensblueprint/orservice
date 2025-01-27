package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "service_at_location_id")
    private ServiceAtLocation serviceAtLocation;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "department")
    private String department;

    @Column(name = "email")
    private String email;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "contact")
    private List<Phone> phones = new ArrayList<>();

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (Phone phone : phones) {
            phone.setContact(null);
        }
    }

    public ContactDTO toDTO() {
        return ContactDTO.builder()
                .id(this.id)
                .organizationId(this.organization == null ? null : this.organization.getId())
                .serviceId(this.service == null ? null : this.service.getId())
                .serviceAtLocationId(this.serviceAtLocation == null ? null : this.serviceAtLocation.getId())
                .locationId(this.location == null ? null : this.location.getId())
                .name(this.name)
                .title(this.title)
                .department(this.department)
                .email(this.email)
                .phones(this.phones.stream().map(Phone::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
