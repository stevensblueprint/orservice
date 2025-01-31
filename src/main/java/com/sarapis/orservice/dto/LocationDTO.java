package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.LocationType;
import com.sarapis.orservice.entity.core.Organization;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private String id;

    private String organizationId;

    private LocationType locationType;
    private String url;
    private String name;
    private String alternateName;
    private String description;
    private String transportation;
    private int latitude;
    private int longitude;
    private String externalIdentifier;
    private String externalIdentifierType;

    private List<LanguageDTO> languages = new ArrayList<>();
    private List<AddressDTO> addresses = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<AccessibilityDTO> accessibility = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<ScheduleDTO> schedules = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Location toEntity(Organization organization) {
        Location location = Location.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .locationType(this.locationType)
                .url(this.url).name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .transportation(this.transportation)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .externalIdentifier(this.externalIdentifier)
                .externalIdentifierType(this.externalIdentifierType)
                .build();
        location.setLanguages(this.languages.stream().map(e -> e.toEntity(null, location, null)).toList());
        location.setAddresses(this.addresses.stream().map(e -> e.toEntity(location)).toList());
        location.setContacts(this.contacts.stream().map(e -> e.toEntity(null, null, null, location)).toList());
        location.setAccessibility(this.accessibility.stream().map(e -> e.toEntity(location)).toList());
        location.setPhones(this.phones.stream().map(e -> e.toEntity(location, null, null, null, null)).toList());
        location.setSchedules(this.schedules.stream().map(e -> e.toEntity(null, location, null)).toList());
        return location;
    }
}
