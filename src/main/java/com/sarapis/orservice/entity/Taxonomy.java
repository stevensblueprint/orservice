package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.TaxonomyDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taxonomy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Taxonomy {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "uri")
    private String uri;

    @Column(name = "version")
    private String version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();

    public TaxonomyDTO toDTO() {
        return TaxonomyDTO.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .uri(this.uri)
                .version(this.version)
                .metadata(this.metadata.stream().map(Metadata::toDTO).toList())
                .build();
    }
}
