package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Accessibility;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessibilityDTO {
    private String id;
    private String description;
    private String details;
    private String url;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Accessibility toEntity() {
        return Accessibility.builder()
                .id(this.id)
                .description(this.description)
                .details(this.details)
                .url(this.url)
                .build();
    }
}
