package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.AccessibilityDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "details")
    private String details;

    @Column(name = "url")
    private String url;

    public AccessibilityDTO toDTO() {
        return AccessibilityDTO.builder()
                .id(this.id)
                .description(this.description)
                .details(this.details)
                .url(this.url)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
