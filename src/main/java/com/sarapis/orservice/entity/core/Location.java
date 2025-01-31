package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    //================================================================================
    // Attributes
    //================================================================================

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false)
    private LocationType locationType;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description")
    private String description;

    @Column(name = "transportation")
    private String transportation;

    @Column(name = "latitude")
    private Integer latitude;

    @Column(name = "longitude")
    private Integer longitude;

    @Column(name = "external_identifier")
    private String externalIdentifier;

    @Column(name = "external_identifier_type")
    private String externalIdentifierType;

    //================================================================================
    // Relations
    //================================================================================

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "location")
    private List<ServiceAtLocation> serviceAtLocations;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Language> languages;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Address> addresses;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Contact> contacts;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Accessibility> accessibility;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Phone> phones;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Schedule> schedules;

    //================================================================================
    // Methods
    //================================================================================

    @PreRemove
    public void preRemove() {
        if (this.organization != null) {
            this.organization.getLocations().remove(this);
        }
        for (ServiceAtLocation serviceAtLocation : this.serviceAtLocations) {
            serviceAtLocation.setLocation(null);
        }
        for (Language language : this.languages) {
            language.setLocation(null);
        }
        for (Address address : this.addresses) {
            address.setLocation(null);
        }
        for (Contact contact : this.contacts) {
            contact.setLocation(null);
        }
        for (Accessibility accessibility : this.accessibility) {
            accessibility.setLocation(null);
        }
        for (Phone phone : this.phones) {
            phone.setLocation(null);
        }
        for (Schedule schedule : this.schedules) {
            schedule.setLocation(null);
        }
    }

    public LocationDTO toDTO() {
        return LocationDTO.builder()
                .id(this.id)
                .organizationId(this.organization == null ? null : this.organization.getId())
                .locationType(this.locationType)
                .url(this.url)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .transportation(this.transportation)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .externalIdentifier(this.externalIdentifier)
                .externalIdentifierType(this.externalIdentifierType)
                .languages(this.languages.stream().map(Language::toDTO).toList())
                .addresses(this.addresses.stream().map(Address::toDTO).toList())
                .contacts(this.contacts.stream().map(Contact::toDTO).toList())
                .accessibility(this.accessibility.stream().map(Accessibility::toDTO).toList())
                .phones(this.phones.stream().map(Phone::toDTO).toList())
                .schedules(this.schedules.stream().map(Schedule::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
