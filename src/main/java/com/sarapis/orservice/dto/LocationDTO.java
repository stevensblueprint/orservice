package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.LocationType;
import lombok.*;

import java.util.List;

/**
 * Returned response for a location entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#location">Reference</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private String id;
    private LocationType locationType;
    private String url;
    private String organizationId;
    private String name;
    private String alternateName;
    private String description;
    private String transportation;
    private int latitude;
    private int longitude;
    private String externalIdentifier;
    private String externalIdentifierType;
    private List<LanguageDTO> languages;
    private List<AddressDTO> addresses;
    private List<ContactDTO> contacts;
    private List<AccessibilityDTO> accessibility;
    private List<PhoneDTO> phones;
    private List<ScheduleDTO> schedules;
    private List<AttributeDTO> attributes;
    private List<MetadataDTO> metadata;
}
