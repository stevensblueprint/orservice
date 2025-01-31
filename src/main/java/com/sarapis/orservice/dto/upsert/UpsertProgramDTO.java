package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.Program;
import lombok.*;

import java.util.UUID;

/**
 * The request body when inserting or updating a program entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertProgramDTO {
    private String name;
    private String alternateName;
    private String description;
    private String organizationId;

    public Program create() {
        return Program.builder()
                .id(UUID.randomUUID().toString())
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .build();
    }

    public Program merge(Program program) {
        return Program.builder()
                .id(program.getId())
                .name(this.name == null ? program.getName() : this.name)
                .alternateName(this.alternateName == null ? program.getAlternateName() : this.alternateName)
                .description(this.description == null ? program.getDescription() : this.description)
                .services(program.getServices())
                .organization(program.getOrganization())
                .build();
    }
}
