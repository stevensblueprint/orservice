package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.UrlDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "label")
    private String label;

    @Column(name = "url", nullable = false)
    private String url;

    public UrlDTO toDTO() {
        return UrlDTO.builder()
                .id(this.id)
                .label(this.label)
                .url(this.url)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
