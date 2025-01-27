package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Accessibility;
import com.sarapis.orservice.entity.core.Location;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessibilityDTO {
    private String id;

    private String locationId;

    private String description;
    private String details;
    private String url;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Accessibility toEntity(Location location) {
        return Accessibility.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .location(location)
                .description(this.description).details(this.details)
                .url(this.url)
                .build();
    }
}
