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
                .locationType(locationType)
                .url(url)
                .name(name)
                .alternateName(alternateName)
                .description(description)
                .transportation(transportation)
                .latitude(latitude)
                .longitude(longitude)
                .externalIdentifier(externalIdentifier)
                .externalIdentifierType(externalIdentifierType)
                .build();
    }

    public Location merge(Location location) {
        return Location.builder()
                .locationType(locationType == null ? location.getLocationType() : locationType)
                .url(url == null ? location.getUrl() : url)
                .name(name == null ? location.getName() : name)
                .alternateName(alternateName == null ? location.getAlternateName() : alternateName)
                .description(description == null ? location.getDescription() : description)
                .transportation(transportation == null ? location.getTransportation() : transportation)
                .latitude(latitude == null ? location.getLatitude() : latitude)
                .longitude(longitude == null ? location.getLongitude() : longitude)
                .externalIdentifier(externalIdentifier == null ? location.getExternalIdentifier() : externalIdentifier)
                .externalIdentifierType(externalIdentifierType == null ? location.getExternalIdentifierType() : externalIdentifierType)
                .build();
    }
}
