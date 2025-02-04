package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.util.Utility;
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
                .name(Utility.getOrDefault(this.name, program.getName()))
                .alternateName(Utility.getOrDefault(this.alternateName, program.getAlternateName()))
                .description(Utility.getOrDefault(this.description, program.getDescription()))
                .services(program.getServices())
                .organization(program.getOrganization())
                .build();
    }
}
