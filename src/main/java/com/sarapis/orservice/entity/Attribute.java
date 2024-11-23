package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.AttributeDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "link_type")
    private String linkType;

    @Column(name = "link_entity")
    private String linkEntity;

    @Column(name = "value")
    private String value;

    @OneToOne
    @JoinColumn(name = "taxonomy_term_id")
    private TaxonomyTerm taxonomyTerm = null;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();

    @Column(name = "label")
    private String label;

    public AttributeDTO toDTO() {
        return AttributeDTO.builder()
                .id(this.id)
                .linkEntity(this.linkEntity)
                .value(this.value)
                .taxonomyTerm(this.taxonomyTerm != null ? this.taxonomyTerm.toDTO() : null)
                .metadata(this.metadata.stream().map(Metadata::toDTO).toList())
                .label(this.label)
                .build();
    }
}
