package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.LocationType;
import com.sarapis.orservice.entity.core.Organization;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    @NotNull
    private String id;

    @NotBlank
    private String organizationId;

    @NotNull
    private LocationType locationType;

    @Size(max = 255, message = "URL should not exceed 255 characters")
    private String url;
    private String name;
    private String alternateName;
    private String description;
    private String transportation;

    @Min(value = -90, message = "Latitude must be at least -90")
    @Max(value = 90, message = "Latitude must be at most 90")
    private int latitude;

    @Min(value = -180, message = "Longitude must be at least -180")
    @Max(value = 180, message = "Longitude must be at most 180")
    private int longitude;

    private String externalIdentifier;
    private String externalIdentifierType;

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<LanguageDTO> languages = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<AddressDTO> addresses = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ContactDTO> contacts = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<AccessibilityDTO> accessibility = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<PhoneDTO> phones = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ScheduleDTO> schedules = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<Object> attributes = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<Object> metadata = new ArrayList<>();

    public Location toEntity() {
        return Location.builder()
                .id(this.id)
                .organization(this.organizationId == null? null : Organization.builder().id(this.organizationId).build())
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
}
