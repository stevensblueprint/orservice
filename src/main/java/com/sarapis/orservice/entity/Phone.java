package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "service_at_location_id")
    private ServiceAtLocation serviceAtLocation;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "extension")
    private String extension;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "phone")
    private List<Language> languages = new ArrayList<>();

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (Language language : languages) {
            language.setPhone(null);
        }
    }

    public PhoneDTO toDTO() {
        return PhoneDTO.builder()
                .id(this.id)
                .locationId(this.location == null ? null : this.location.getId())
                .serviceId(this.service == null ? null : this.service.getId())
                .organizationId(this.organization == null ? null : this.organization.getId())
                .contactId(this.contact == null ? null : this.contact.getId())
                .serviceAtLocationId(this.serviceAtLocation == null ? null : this.serviceAtLocation.getId())
                .number(this.number)
                .extension(this.extension)
                .type(this.type)
                .description(this.description)
                .languages(this.languages.stream().map(Language::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
