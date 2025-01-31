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
                .name(name)
                .alternateName(alternateName)
                .description(description)
                .build();
    }

    public Program merge(Program program) {
        return Program.builder()
                .id(program.getId())
                .name(name == null ? program.getName() : name)
                .alternateName(alternateName == null ? program.getAlternateName() : alternateName)
                .description(description == null ? program.getDescription() : description)
                .services(program.getServices())
                .organization(program.getOrganization())
                .build();
    }
}
