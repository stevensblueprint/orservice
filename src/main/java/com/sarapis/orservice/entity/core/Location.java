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
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

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
    private int latitude;

    @Column(name = "longitude")
    private int longitude;

    @Column(name = "external_identifier")
    private String externalIdentifier;

    @Column(name = "external_identifier_type")
    private String externalIdentifierType;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "location")
    private ServiceAtLocation serviceAtLocation;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Language> languages = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Accessibility> accessibility = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Schedule> schedules = new ArrayList<>();

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (Language language : languages) {
            language.setLocation(null);
        }
        for (Address address : addresses) {
            address.setLocation(null);
        }
        for (Contact contact : contacts) {
            contact.setLocation(null);
        }
        for (Accessibility accessibility : accessibility) {
            accessibility.setLocation(null);
        }
        for (Phone phone : phones) {
            phone.setLocation(null);
        }
        for (Schedule schedule : schedules) {
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
