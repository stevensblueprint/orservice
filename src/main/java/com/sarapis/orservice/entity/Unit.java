package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "scheme")
    private String scheme;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "uri")
    private String uri;

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes;

    @OneToMany
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata;
}
