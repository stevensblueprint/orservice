package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.AttributeDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

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

    @Column(name = "link_id", nullable = false)
    private String linkId;

    @Column(name = "link_type")
    private String linkType;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_entity", nullable = false)
    private LinkEntity linkEntity;

    @Column(name = "value")
    private String value;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "taxonomy_term_id", nullable = false)
    private TaxonomyTerm taxonomyTerm;

    @Column(name = "label")
    private String label;

    public AttributeDTO toDTO() {
        return AttributeDTO.builder()
                .id(this.id)
                .linkId(this.linkId)
                .linkEntity(this.linkEntity)
                .value(this.value)
                .taxonomyTerm(this.taxonomyTerm != null ? this.taxonomyTerm.toDTO() : null)
                .metadata(new ArrayList<>())
                .label(this.label)
                .build();
    }
}
