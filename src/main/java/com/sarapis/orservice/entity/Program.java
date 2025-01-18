package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ProgramDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "program")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Program {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description", nullable = false)
    private String description;

    public ProgramDTO toDTO() {
        return ProgramDTO.builder()
                .id(this.id)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
