package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.MetaTableDescriptionDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "meta_table_description")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaTableDescription {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "language")
    private String language;

    @Column(name = "character_set")
    private String characterSet;

    public MetaTableDescriptionDTO toDTO() {
        return MetaTableDescriptionDTO.builder()
                .id(this.id)
                .name(this.name)
                .language(this.language)
                .characterSet(this.characterSet)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
