package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.PhoneDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "extension")
    private String extension;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "phone_id")
    private List<Language> languages = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();

    public PhoneDTO toDTO() {
        return PhoneDTO.builder()
                .id(this.id)
                .number(this.number)
                .extension(this.extension)
                .type(this.type)
                .description(this.description)
                .languages(this.languages.stream().map(Language::toDTO).toList())
                .attributes(this.attributes.stream().map(Attribute::toDTO).toList())
                .metadata(this.metadata.stream().map(Metadata::toDTO).toList())
                .build();
    }
}
