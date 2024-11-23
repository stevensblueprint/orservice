package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.MetadataDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metadata {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "last_action_date", nullable = false)
    private LocalDate lastActionDate;

    @Column(name = "last_action_type", nullable = false)
    private String lastActionType;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "previous_value", nullable = false)
    private String previousValue;

    @Column(name = "replacement_value", nullable = false)
    private String replacementValue;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    public MetadataDTO toDTO() {
        return MetadataDTO.builder()
                .id(this.id)
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
