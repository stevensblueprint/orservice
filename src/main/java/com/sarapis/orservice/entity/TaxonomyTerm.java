package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "taxonomy_term")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxonomyTerm {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private TaxonomyTerm parent = null;

    @Column(name = "taxonomy")
    private String taxonomy;

    @ManyToOne
    @JoinColumn(name = "taxonomy_id")
    private Taxonomy taxonomyDetail = null;

    @Column(name = "language")
    private String language;

    @Column(name = "term_uri")
    private String termUri;

    public TaxonomyTermDTO toDTO() {
        return TaxonomyTermDTO.builder()
                .id(this.id)
                .code(this.code)
                .name(this.name)
                .description(this.description)
                .parent(this.parent != null ? this.parent.toDTO() : null)
                .taxonomy(this.taxonomy)
                .taxonomyDetail(this.taxonomyDetail != null ? this.taxonomyDetail.toDTO() : null)
                .language(this.language)
                .termUri(this.termUri)
                .metadata(new ArrayList<>())
                .build();
    }
}
