package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Program;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramDTO {
    private String id;
    private String name;
    private String alternateName;
    private String description;
    private String organizationId;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Program toEntity() {
        return Program.builder()
                .id(this.id)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .build();
    }
}
