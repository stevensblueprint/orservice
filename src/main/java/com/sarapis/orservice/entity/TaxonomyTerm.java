package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taxonomy_term")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxonomyTerm {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TaxonomyTerm parent = null;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "taxonomy")
    private String taxonomy;

    @Column(name = "language")
    private String language;

    @Column(name = "term_uri")
    private String termUri;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "taxonomy_id")
    private Taxonomy taxonomyDetail = null;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "parent")
    private List<TaxonomyTerm> children = new ArrayList<>();

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (TaxonomyTerm taxonomyTerm : children) {
            taxonomyTerm.setParent(null);
        }
    }

    public TaxonomyTermDTO toDTO() {
        return TaxonomyTermDTO.builder()
                .id(this.id)
                .parentId(this.parent == null ? null : this.parent.getId())
                .code(this.code)
                .name(this.name)
                .description(this.description)
                .taxonomy(this.taxonomy)
                .language(this.language)
                .termUri(this.termUri)
                .taxonomyDetail(this.taxonomyDetail == null ? null : this.taxonomyDetail.toDTO())
                .metadata(new ArrayList<>())
                .build();
    }
}
