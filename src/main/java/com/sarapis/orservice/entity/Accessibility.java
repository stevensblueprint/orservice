package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.entity.core.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "accessibility")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accessibility {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "description")
    private String description;

    @Column(name = "details")
    private String details;

    @Column(name = "url")
    private String url;

    public AccessibilityDTO toDTO() {
        return AccessibilityDTO.builder()
                .id(this.id)
                .locationId(this.location == null ? null : this.location.getId())
                .description(this.description)
                .details(this.details)
                .url(this.url)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
