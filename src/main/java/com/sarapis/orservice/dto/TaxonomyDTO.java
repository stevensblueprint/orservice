package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Taxonomy;
import com.sarapis.orservice.entity.TaxonomyTerm;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxonomyDTO {
    private String id;

    private String name;
    private String description;
    private String uri;
    private String version;

    private List<MetadataDTO> metadata = new ArrayList<>();

    public Taxonomy toEntity() {
        return Taxonomy.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .name(this.name)
                .description(this.description)
                .uri(this.uri)
                .version(this.version)
                .build();
    }
}
