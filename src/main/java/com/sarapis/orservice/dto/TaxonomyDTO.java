package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Taxonomy;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
            .id(this.id)
            .name(this.name)
            .description(this.description)
            .uri(this.uri)
            .version(this.version)
            .build();
  }
}
