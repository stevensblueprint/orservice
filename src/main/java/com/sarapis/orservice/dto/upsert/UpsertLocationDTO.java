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
    private int latitude;
    private int longitude;
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
}
