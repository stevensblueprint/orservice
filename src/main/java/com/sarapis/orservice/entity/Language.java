package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.LanguageDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "note")
    private String note;

    public LanguageDTO toDTO() {
        return LanguageDTO.builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .note(this.note)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
