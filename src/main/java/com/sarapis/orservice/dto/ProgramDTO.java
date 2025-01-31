package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.entity.core.Organization;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramDTO {
    private String id;

    private String organizationId;

    private String name;
    private String alternateName;
    private String description;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Program toEntity(Organization organization) {
        return Program.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .build();
    }
}
