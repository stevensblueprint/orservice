package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.LocationType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private String id;
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

    public Location toEntity() {
        return Location.builder()
                .id(this.id)
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
                .languages(this.languages.stream().map(LanguageDTO::toEntity).toList())
                .addresses(this.addresses.stream().map(AddressDTO::toEntity).toList())
                .contacts(this.contacts.stream().map(ContactDTO::toEntity).toList())
                .accessibility(this.accessibility.stream().map(AccessibilityDTO::toEntity).toList())
                .phones(this.phones.stream().map(PhoneDTO::toEntity).toList())
                .schedules(this.schedules.stream().map(ScheduleDTO::toEntity).toList())
                .attributes(this.attributes.stream().map(AttributeDTO::toEntity).toList())
                .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
                .build();
    }
}
