package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.LocationType;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The request body when inserting or updating a location entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertLocationDTO {
    private LocationType locationType;
    private String url;
    private String name;
    private String alternateName;
    private String description;
    private String transportation;
    private Integer latitude;
    private Integer longitude;
    private String externalIdentifier;
    private String externalIdentifierType;
    private String organizationId;
    private List<String> languages;
    private List<String> addresses;
    private List<String> phones;
    private List<String> contacts;
    private List<String> accessibility;
    private List<String> schedules;

    public Location create() {
        return Location.builder()
                .id(UUID.randomUUID().toString())
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
                .build();
    }

    public Location merge(Location location) {
        return Location.builder()
                .id(location.getId())
                .locationType(this.locationType == null ? location.getLocationType() : this.locationType)
                .url(this.url == null ? location.getUrl() : this.url)
                .name(this.name == null ? location.getName() : this.name)
                .alternateName(this.alternateName == null ? location.getAlternateName() : this.alternateName)
                .description(this.description == null ? location.getDescription() : this.description)
                .transportation(this.transportation == null ? location.getTransportation() : this.transportation)
                .latitude(this.latitude == null ? location.getLatitude() : this.latitude)
                .longitude(this.longitude == null ? location.getLongitude() : this.longitude)
                .externalIdentifier(this.externalIdentifier == null ? location.getExternalIdentifier() : this.externalIdentifier)
                .externalIdentifierType(this.externalIdentifierType == null ? location.getExternalIdentifierType() : this.externalIdentifierType)
                .organization(location.getOrganization())
                .serviceAtLocations(location.getServiceAtLocations())
                .languages(location.getLanguages())
                .addresses(location.getAddresses())
                .contacts(location.getContacts())
                .accessibility(location.getAccessibility())
                .phones(location.getPhones())
                .schedules(location.getSchedules())
                .build();
    }
}
