package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.ResourceType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetadataDTO {
    private String id;

    private String resourceId;

    private ResourceType resourceType;
    private LocalDate lastActionDate;
    private String lastActionType;
    private String fieldName;
    private String previousValue;
    private String replacementValue;
    private String updatedBy;

    public Metadata toEntity(String resourceId) {
        return Metadata.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .resourceId(resourceId)
                .resourceType(this.resourceType)
                .lastActionDate(this.lastActionDate)
                .lastActionType(this.lastActionType)
                .fieldName(this.fieldName)
                .previousValue(this.previousValue)
                .replacementValue(this.replacementValue)
                .updatedBy(this.updatedBy)
                .build();
    }
}
