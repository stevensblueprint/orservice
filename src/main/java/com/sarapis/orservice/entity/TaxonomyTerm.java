package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
    private TaxonomyTerm parent;

    @Column(name = "taxonomy")
    private String taxonomyName;

    @ManyToOne
    @JoinColumn(name = "taxonomy_id")
    private Taxonomy taxonomy;

    @Column(name = "language")
    private String language;

    @Column(name = "term_uri")
    private String termUri;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();
}
