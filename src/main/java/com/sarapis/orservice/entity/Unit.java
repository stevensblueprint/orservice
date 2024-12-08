package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.UnitDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "scheme")
    private String scheme;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "uri")
    private String uri;

    public UnitDTO toDTO() {
        return UnitDTO.builder()
                .id(this.id)
                .name(this.name)
                .scheme(this.scheme)
                .identifier(this.identifier)
                .uri(this.uri)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
