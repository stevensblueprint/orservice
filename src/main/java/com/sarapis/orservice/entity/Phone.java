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

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "phone_id")
    private List<Language> languages = new ArrayList<>();

    public PhoneDTO toDTO() {
        return PhoneDTO.builder()
                .id(this.id)
                .number(this.number)
                .extension(this.extension)
                .type(this.type)
                .description(this.description)
                .languages(this.languages.stream().map(Language::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
