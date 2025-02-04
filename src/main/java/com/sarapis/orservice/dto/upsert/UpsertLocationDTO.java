package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.LocationType;
import com.sarapis.orservice.util.Utility;
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
                .locationType(Utility.getOrDefault(this.locationType, location.getLocationType()))
                .url(Utility.getOrDefault(this.url, location.getUrl()))
                .name(Utility.getOrDefault(this.name, location.getName()))
                .alternateName(Utility.getOrDefault(this.alternateName, location.getAlternateName()))
                .description(Utility.getOrDefault(this.description, location.getDescription()))
                .transportation(Utility.getOrDefault(this.transportation, location.getTransportation()))
                .latitude(Utility.getOrDefault(this.latitude, location.getLatitude()))
                .longitude(Utility.getOrDefault(this.longitude, location.getLongitude()))
                .externalIdentifier(Utility.getOrDefault(this.externalIdentifier, location.getExternalIdentifier()))
                .externalIdentifierType(Utility.getOrDefault(this.externalIdentifierType, location.getExternalIdentifierType()))
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
